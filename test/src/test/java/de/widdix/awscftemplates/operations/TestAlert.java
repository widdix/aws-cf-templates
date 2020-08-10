package de.widdix.awscftemplates.operations;

import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestAlert extends ACloudFormationTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String stackName = "alert-" + this.random8String();
        try {
            this.createStack(context, stackName,
                    "operations/alert.yaml"
            );
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(context, stackName);
        }
    }

}
