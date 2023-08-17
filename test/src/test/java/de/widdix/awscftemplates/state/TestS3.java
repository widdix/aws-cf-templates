package de.widdix.awscftemplates.state;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestS3 extends ACloudFormationTest {

    private void test(final String access) {
        final Context context = new Context();
        final String stackName = "s3-" + this.random8String();
        try {
            this.createStack(context, stackName, "state/s3.yaml",
                    new Parameter().withParameterKey("Access").withParameterValue(access));
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(context, stackName);
        }
    }

    @Test
    public void testPrivate() {
        this.test("Private");
    }

    @Test
    public void testPublicRead() {
        this.test("PublicRead");
    }

    @Test
    public void testPublicWrite() {
        this.test("PublicWrite");
    }

    @Test
    public void testPublicReadAndWrite() {
        this.test("PublicReadAndWrite");
    }

    @Test
    public void testCloudFrontRead() {
        this.test("CloudFrontRead");
    }

    @Test
    public void testCloudFrontAccessLogWrite() {
        this.test("CloudFrontAccessLogWrite");
    }

    @Test
    public void testElbAccessLogWrite() {
        this.test("ElbAccessLogWrite");
    }

    @Test
    public void testS3AccessLogWrite() {
        this.test("S3AccessLogWrite");
    }

    @Test
    public void testConfigWrite() {
        this.test("ConfigWrite");
    }

    @Test
    public void testCloudTrailWrite() {
        this.test("CloudTrailWrite");
    }

    @Test
    public void testVpcEndpointRead() {
        this.test("VpcEndpointRead");
    }

    @Test
    public void testFlowLogWrite() {
        this.test("FlowLogWrite");
    }

}