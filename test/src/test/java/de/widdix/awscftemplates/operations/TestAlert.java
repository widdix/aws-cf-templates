package de.widdix.awscftemplates.operations;

import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestAlert extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "alert-" + this.random8String();
        try {
            this.createStack(stackName,
                    "operations/alert.yaml"
            );
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(stackName);
        }
    }

}
