package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import org.junit.Test;

public class TestVPC3AZs extends AVPCTest {

    @Test
    public void test() {
        final String stackName = "vpc-3azs-" + this.random8String();
        try {
            this.createStack(stackName,
                    "vpc/vpc-3azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue("10")
            );
            this.testVPCSubnetInternetAccess(stackName, "SubnetAPublic");
            this.testVPCSubnetInternetAccess(stackName, "SubnetBPublic");
            this.testVPCSubnetInternetAccess(stackName, "SubnetCPublic");
        } finally {
            this.deleteStack(stackName);
        }
    }

}
