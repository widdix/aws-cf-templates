package de.widdix.awscftemplates.state;

import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestS3 extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "s3-" + this.random8String();
        try {
            this.createStack(stackName, "state/s3.yaml");
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(stackName);
        }
    }

}
