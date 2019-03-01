package de.widdix.awscftemplates;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;
import com.amazonaws.services.cloudformation.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

public abstract class ACloudFormationTest extends AAWSTest {

    public static String readFile(String path, Charset encoding) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final AmazonCloudFormation cf = AmazonCloudFormationClientBuilder.standard().withCredentials(this.credentialsProvider).build();

    public ACloudFormationTest() {
        super();
    }

    protected final void createStack(final String stackName, final String template, final Parameter... parameters) {
        CreateStackRequest req = new CreateStackRequest()
                .withStackName(stackName)
                .withParameters(parameters)
                .withCapabilities(Capability.CAPABILITY_IAM);
        if (Config.has(Config.Key.TEMPLATE_DIR)) {
            final String dir = Config.get(Config.Key.TEMPLATE_DIR);
            if (Config.has(Config.Key.BUCKET_NAME)) {
                final String bucketName = Config.get(Config.Key.BUCKET_NAME);
                final String bucketRegion = Config.get(Config.Key.BUCKET_REGION);
                final AmazonS3 s3local = AmazonS3ClientBuilder.standard().withCredentials(this.credentialsProvider).withRegion(bucketRegion).build();
                s3local.putObject(bucketName, stackName, new File(dir + template));
                req = req.withTemplateURL("https://s3-" + bucketRegion + ".amazonaws.com/" + bucketName + "/" + stackName);
            } else {
                final String body = readFile(dir + template, Charset.forName("UTF-8"));
                req = req.withTemplateBody(body);
            }
        } else {
            req = req.withTemplateURL("https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/" + template);
        }
        this.cf.createStack(req);
        this.waitForStack(stackName, FinalStatus.CREATE_COMPLETE);
    }

    protected enum FinalStatus {
        CREATE_COMPLETE(StackStatus.CREATE_COMPLETE, false, true, StackStatus.CREATE_IN_PROGRESS),
        DELETE_COMPLETE(StackStatus.DELETE_COMPLETE, true, false, StackStatus.DELETE_IN_PROGRESS);

        private final StackStatus finalStatus;
        private final boolean notFoundIsFinalStatus;
        private final boolean notFoundIsIntermediateStatus;
        private final Set<StackStatus> intermediateStatus;

        FinalStatus(StackStatus finalStatus, boolean notFoundIsFinalStatus, boolean notFoundIsIntermediateStatus, StackStatus... intermediateStatus) {
            this.finalStatus = finalStatus;
            this.notFoundIsFinalStatus = notFoundIsFinalStatus;
            this.notFoundIsIntermediateStatus = notFoundIsIntermediateStatus;
            this.intermediateStatus = new HashSet<>(Arrays.asList(intermediateStatus));
        }
    }

    private List<StackEvent> getStackEvents(final String stackName) {
        final List<StackEvent> events = new ArrayList<>();
        String nextToken = null;
        do {
            try {
                final DescribeStackEventsResult res = this.cf.describeStackEvents(new DescribeStackEventsRequest().withStackName(stackName).withNextToken(nextToken));
                events.addAll(res.getStackEvents());
                nextToken = res.getNextToken();
            } catch (final AmazonServiceException e) {
                if (e.getErrorMessage().equals("Stack [" + stackName + "] does not exist")) {
                    nextToken = null;
                } else {
                    throw e;
                }
            }
        } while (nextToken != null);
        Collections.reverse(events);
        return events;
    }

    private void waitForStack(final String stackName, final FinalStatus finalStackStatus) {
        System.out.println("waitForStack[" + stackName + "]: to reach status " + finalStackStatus.finalStatus);
        final List<StackEvent> eventsDisplayed = new ArrayList<>();
        while (true) {
            try {
                Thread.sleep(20000);
            } catch (final InterruptedException e) {
                // continue
            }
            final List<StackEvent> events = getStackEvents(stackName);
            for (final StackEvent event : events) {
                boolean displayed = false;
                for (final StackEvent eventDisplayed : eventsDisplayed) {
                    if (event.getEventId().equals(eventDisplayed.getEventId())) {
                        displayed = true;
                    }
                }
                if (!displayed) {
                    System.out.println("waitForStack[" + stackName + "]: " + event.getTimestamp().toString() + " " + event.getLogicalResourceId() + " " + event.getResourceStatus() + " " + event.getResourceStatusReason());
                    eventsDisplayed.add(event);
                }
            }
            try {
                final DescribeStacksResult res = this.cf.describeStacks(new DescribeStacksRequest().withStackName(stackName));
                final StackStatus currentStatus = StackStatus.fromValue(res.getStacks().get(0).getStackStatus());
                if (finalStackStatus.finalStatus == currentStatus) {
                    System.out.println("waitForStack[" + stackName + "]: final status reached.");
                    return;
                } else {
                    if (finalStackStatus.intermediateStatus.contains(currentStatus)) {
                        System.out.println("waitForStack[" + stackName + "]: continue to wait (still in intermediate status " + currentStatus + ") ...");
                    } else {
                        throw new RuntimeException("waitForStack[" + stackName + "]: reached invalid intermediate status " + currentStatus + ".");
                    }
                }
            } catch (final AmazonServiceException e) {
                if (e.getErrorMessage().equals("Stack with id " + stackName + " does not exist")) {
                    if (finalStackStatus.notFoundIsFinalStatus) {
                        System.out.println("waitForStack[" + stackName + "]: final  reached (not found).");
                        return;
                    } else {
                        if (finalStackStatus.notFoundIsIntermediateStatus) {
                            System.out.println("waitForStack[" + stackName + "]: continue to wait (stack not found) ...");
                        } else {
                            throw new RuntimeException("waitForStack[" + stackName + "]: stack not found.");
                        }
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    protected final Map<String, String> getStackOutputs(final String stackName) {
        final DescribeStacksResult res = this.cf.describeStacks(new DescribeStacksRequest().withStackName(stackName));
        final List<Output> outputs = res.getStacks().get(0).getOutputs();
        final Map<String, String> map = new HashMap<>(outputs.size());
        for (final Output output : outputs) {
            map.put(output.getOutputKey(), output.getOutputValue());
        }
        return map;
    }

    protected final String getStackOutputValue(final String stackName, final String outputKey) {
        return this.getStackOutputs(stackName).get(outputKey);
    }

    protected final void deleteStackAndRetryOnFailure(final String stackName) {
        final Callable<Boolean> callable = () -> {
            this.deleteStack(stackName);
            return true;
        };
        this.retry(callable);
    }

    protected final void deleteStack(final String stackName) {
        if (Config.get(Config.Key.DELETION_POLICY).equals("delete")) {
            this.cf.deleteStack(new DeleteStackRequest().withStackName(stackName));
            if (Config.has(Config.Key.BUCKET_NAME)) {
                final AmazonS3 s3local = AmazonS3ClientBuilder.standard().withCredentials(this.credentialsProvider).withRegion(Config.get(Config.Key.BUCKET_REGION)).build();
                s3local.deleteObject(Config.get(Config.Key.BUCKET_NAME), stackName);
            }
            this.waitForStack(stackName, FinalStatus.DELETE_COMPLETE);
        }
    }

}
