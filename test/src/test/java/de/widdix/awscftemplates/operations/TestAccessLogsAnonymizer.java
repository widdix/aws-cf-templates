package de.widdix.awscftemplates.operations;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestAccessLogsAnonymizer extends ACloudFormationTest {

    @Test
    public void cloudfront() {
        final Context context = new Context();
        final String s3StackName = "s3-" + this.random8String();
        final String anonymizerStackName = "anonymizer-" + this.random8String();
        try {
            this.createStack(context, s3StackName,
                    "state/s3.yaml",
                    new Parameter().withParameterKey("Access").withParameterValue("CloudFrontAccessLogWrite")
            );
            try {
                this.createStack(context, anonymizerStackName,
                        "operations/cloudfront-access-logs-anonymizer.yaml",
                        new Parameter().withParameterKey("ParentS3Stack").withParameterValue(s3StackName)
                );
                final String functionARN = this.getStackOutputValue(anonymizerStackName, "FunctionARN");
                this.updateStack(context, s3StackName,
                        "state/s3.yaml",
                        new Parameter().withParameterKey("Access").withParameterValue("CloudFrontAccessLogWrite"),
                        new Parameter().withParameterKey("LambdaFunctionArn").withParameterValue(functionARN)
                        );
                // TODO upload file and test if IP addresses are anonymized
            } finally {
                this.deleteStack(context, anonymizerStackName);
            }
        } finally {
            this.deleteStack(context, s3StackName);
        }
    }

    @Test
    public void alb() {
        final Context context = new Context();
        final String s3StackName = "s3-" + this.random8String();
        final String anonymizerStackName = "anonymizer-" + this.random8String();
        try {
            this.createStack(context, s3StackName,
                    "state/s3.yaml",
                    new Parameter().withParameterKey("Access").withParameterValue("ElbAccessLogWrite")
            );
            try {
                this.createStack(context, anonymizerStackName,
                        "operations/alb-access-logs-anonymizer.yaml",
                        new Parameter().withParameterKey("ParentS3Stack").withParameterValue(s3StackName)
                );
                final String functionARN = this.getStackOutputValue(anonymizerStackName, "FunctionARN");
                this.updateStack(context, s3StackName,
                        "state/s3.yaml",
                        new Parameter().withParameterKey("Access").withParameterValue("CloudFrontAccessLogWrite"),
                        new Parameter().withParameterKey("LambdaFunctionArn").withParameterValue(functionARN)
                );
                // TODO upload file and test if IP addresses are anonymized
            } finally {
                this.deleteStack(context, anonymizerStackName);
            }
        } finally {
            this.deleteStack(context, s3StackName);
        }
    }

}
