package de.widdix.awscftemplates.security;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Ignore;
import org.junit.Test;

public class TestConfig extends ACloudFormationTest {

    @Ignore
    // this test is disabled because only one ConfigurationRecorder is allowed and config is already active in all our AWS accounts
    @Test
    public void test() {
        final Context context = new Context();
        final String stackName = "config-" + this.random8String();
        final String bucketName = "config-" + this.random8String();
        final String bucketPolicy = "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Sid\": \"AWSConfigBucketPermissionsCheck\",\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": {\n" +
                "        \"Service\": [\n" +
                "         \"config.amazonaws.com\"\n" +
                "        ]\n" +
                "      },\n" +
                "      \"Action\": \"s3:GetBucketAcl\",\n" +
                "      \"Resource\": \"arn:aws:s3:::" + bucketName + "\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"Sid\": \" AWSConfigBucketDelivery\",\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": {\n" +
                "        \"Service\": [\n" +
                "         \"config.amazonaws.com\"\n" +
                "        ]\n" +
                "      },\n" +
                "      \"Action\": \"s3:PutObject\",\n" +
                "      \"Resource\": [\n" +
                "        \"arn:aws:s3:::" + bucketName + "/AWSLogs/" + this.getAccount() + "/Config/*\"\n" +
                "      ],\n" +
                "      \"Condition\": {\n" +
                "        \"StringEquals\": {\n" +
                "          \"s3:x-amz-acl\": \"bucket-owner-full-control\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            this.createBucket(bucketName, bucketPolicy);
            try {
                this.createStack(context, stackName,
                        "security/config.yaml",
                        new Parameter().withParameterKey("ExternalConfigBucket").withParameterValue(bucketName)
                );
                // TODO how can we check if this stack works?
            } finally {
                this.deleteStack(context, stackName);
            }
        } finally {
            this.deleteBucket(context, bucketName);
        }
    }

}
