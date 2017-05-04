package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestVPC3AZs extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "vpc-3azs-" + this.random8String();
        try {
            this.createStack(stackName,
                    "vpc/vpc-3azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue("10")
            );
            // TODO how can we check if this stack works? launch an EC2 instance into a public subnet and open google from the instance?
        } finally {
            this.deleteStack(stackName);
        }
    }

}
