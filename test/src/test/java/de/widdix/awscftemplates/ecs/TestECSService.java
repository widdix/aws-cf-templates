package de.widdix.awscftemplates.ecs;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClientBuilder;
import com.amazonaws.services.ecs.model.*;
import de.taimos.httputils.WS;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestECSService extends ACloudFormationTest {

    private final AmazonECS ecs = AmazonECSClientBuilder.standard().withCredentials(this.credentialsProvider).build();

    private String createTaskDefinition(final String family) {
        final PortMapping pm = new PortMapping().withContainerPort(80).withProtocol("tcp");
        final ContainerDefinition cd = new ContainerDefinition().withName("main").withImage("nginx:1.11.5").withMemory(128).withPortMappings(pm).withEssential(true);
        final RegisterTaskDefinitionResult res = this.ecs.registerTaskDefinition(new RegisterTaskDefinitionRequest().withFamily(family).withNetworkMode("bridge").withContainerDefinitions(cd));
        return res.getTaskDefinition().getTaskDefinitionArn();
    }

    private void deleteTaskDefinition(final String taskDefinitionArn) {
        this.ecs.deregisterTaskDefinition(new DeregisterTaskDefinitionRequest().withTaskDefinition(taskDefinitionArn));
    }

    @Test
    public void testClusterAlb() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String taskDefinitionArn = this.createTaskDefinition(stackName);
        try {
            try {
                this.createKey(keyName);
                try {
                    this.createStack(vpcStackName,
                            "vpc/vpc-2azs.yaml",
                            new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                    );
                    this.waitForStack(vpcStackName, ACloudFormationTest.FinalStatus.CREATE_COMPLETE);
                    try {
                        this.createStack(clusterStackName,
                                "ecs/cluster.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                        );
                        this.waitForStack(clusterStackName, ACloudFormationTest.FinalStatus.CREATE_COMPLETE);
                        final String cluster = this.getStackOutputValue(clusterStackName, "Cluster");
                        try {
                            this.createStack(stackName,
                                    "ecs/service-cluster-alb.yaml",
                                    new Parameter().withParameterKey("ParentClusterStack").withParameterValue(clusterStackName),
                                    new Parameter().withParameterKey("TaskDefinitionArn").withParameterValue(taskDefinitionArn)
                            );
                            this.waitForStack(stackName, ACloudFormationTest.FinalStatus.CREATE_COMPLETE);
                            final String url = this.getStackOutputValue(stackName, "URL");
                            final Callable<Boolean> callable = () -> {
                                final HttpResponse response = WS.url(url).timeout(10000).get();
                                // check HTTP response code
                                if (WS.getStatus(response) != 404) {
                                    throw new RuntimeException("404 expected, but saw " + WS.getStatus(response));
                                }
                                return true;
                            };
                            Assert.assertTrue(this.retry(callable));
                        } finally {
                            this.deleteStack(stackName);
                            this.waitForStack(stackName, ACloudFormationTest.FinalStatus.DELETE_COMPLETE);
                        }
                    } finally {
                        this.deleteStack(clusterStackName);
                        this.waitForStack(clusterStackName, ACloudFormationTest.FinalStatus.DELETE_COMPLETE);
                    }
                } finally {
                    this.deleteStack(vpcStackName);
                    this.waitForStack(vpcStackName, ACloudFormationTest.FinalStatus.DELETE_COMPLETE);
                }
            } finally {
                this.deleteKey(keyName);
            }
        } finally {
            this.deleteTaskDefinition(taskDefinitionArn);
        }
    }

    @Test
    public void testDedicatedAlb() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String taskDefinitionArn = this.createTaskDefinition(stackName);
        try {
            try {
                this.createKey(keyName);
                try {
                    this.createStack(vpcStackName,
                            "vpc/vpc-2azs.yaml",
                            new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                    );
                    this.waitForStack(vpcStackName, ACloudFormationTest.FinalStatus.CREATE_COMPLETE);
                    try {
                        this.createStack(clusterStackName,
                                "ecs/cluster.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                        );
                        this.waitForStack(clusterStackName, ACloudFormationTest.FinalStatus.CREATE_COMPLETE);
                        final String cluster = this.getStackOutputValue(clusterStackName, "Cluster");
                        try {
                            this.createStack(stackName,
                                    "ecs/service-dedicated-alb.yaml",
                                    new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                    new Parameter().withParameterKey("ParentClusterStack").withParameterValue(clusterStackName),
                                    new Parameter().withParameterKey("TaskDefinitionArn").withParameterValue(taskDefinitionArn)
                            );
                            this.waitForStack(stackName, ACloudFormationTest.FinalStatus.CREATE_COMPLETE);
                            final String url = this.getStackOutputValue(stackName, "URL");
                            final Callable<String> callable = () -> {
                                final HttpResponse response = WS.url(url).timeout(10000).get();
                                // check HTTP response code
                                if (WS.getStatus(response) != 200) {
                                    throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                                }
                                return WS.getResponseAsString(response);
                            };
                            final String response = this.retry(callable);
                            // check if nginx page appears
                            Assert.assertTrue(response.contains("Welcome to nginx!"));
                        } finally {
                            this.deleteStack(stackName);
                            this.waitForStack(stackName, ACloudFormationTest.FinalStatus.DELETE_COMPLETE);
                        }
                    } finally {
                        this.deleteStack(clusterStackName);
                        this.waitForStack(clusterStackName, ACloudFormationTest.FinalStatus.DELETE_COMPLETE);
                    }
                } finally {
                    this.deleteStack(vpcStackName);
                    this.waitForStack(vpcStackName, ACloudFormationTest.FinalStatus.DELETE_COMPLETE);
                }
            } finally {
                this.deleteKey(keyName);
            }
        } finally {
            this.deleteTaskDefinition(taskDefinitionArn);
        }
    }

}
