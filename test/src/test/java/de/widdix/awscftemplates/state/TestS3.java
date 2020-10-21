package de.widdix.awscftemplates.state;

import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestS3 extends ACloudFormationTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String stackName = "s3-" + this.random8String();
        try {
            this.createStack(context, stackName, "state/s3.yaml");
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(context, stackName);
        }
    }

}