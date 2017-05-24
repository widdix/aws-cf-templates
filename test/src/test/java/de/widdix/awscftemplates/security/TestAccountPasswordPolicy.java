package de.widdix.awscftemplates.security;

import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestAccountPasswordPolicy extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "account-password-policy-" + this.random8String();
        try {
            this.createStack(stackName,
                    "security/account-password-policy.yaml"
            );
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(stackName);
        }
    }

}
