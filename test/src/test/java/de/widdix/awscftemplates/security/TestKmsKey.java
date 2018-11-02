package de.widdix.awscftemplates.security;

import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestKmsKey extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "kms-key-" + this.random8String();
        try {
            this.createStack(stackName, "security/kms-key.yaml");
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(stackName);
        }
    }

}
