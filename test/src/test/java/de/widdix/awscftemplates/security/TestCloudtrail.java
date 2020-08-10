package de.widdix.awscftemplates.security;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestCloudtrail extends ACloudFormationTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String stackName = "cloudtrail-" + this.random8String();
        final String bucketName = "cloudtrail-" + this.random8String();
        final String bucketPolicy = "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [{\n" +
                "    \"Sid\": \"AWSCloudTrailAclCheck\",\n" +
                "    \"Effect\": \"Allow\",\n" +
                "    \"Principal\": {\n" +
                "      \"Service\": \"cloudtrail.amazonaws.com\"\n" +
                "    },\n" +
                "    \"Action\": \"s3:GetBucketAcl\",\n" +
                "    \"Resource\": \"arn:aws:s3:::" + bucketName + "\"\n" +
                "  }, {\n" +
                "    \"Sid\": \"AWSCloudTrailWrite\",\n" +
                "    \"Effect\": \"Allow\",\n" +
                "    \"Principal\": {\n" +
                "      \"Service\": \"cloudtrail.amazonaws.com\"\n" +
                "    },\n" +
                "    \"Action\": \"s3:PutObject\",\n" +
                "    \"Resource\": [\n" +
                "      \"arn:aws:s3:::" + bucketName + "/AWSLogs/" + this.getAccount() + "/*\"\n" +
                "    ],\n" +
                "    \"Condition\": {\n" +
                "      \"StringEquals\": {\n" +
                "        \"s3:x-amz-acl\": \"bucket-owner-full-control\"\n" +
                "      }\n" +
                "    }\n" +
                "  }]\n" +
                "}";
        try {
            this.createBucket(bucketName, bucketPolicy);
            try {
                this.createStack(context, stackName,
                        "security/cloudtrail.yaml",
                        new Parameter().withParameterKey("ExternalTrailBucket").withParameterValue(bucketName)
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
