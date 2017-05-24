package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import org.junit.Test;

public class TestVPC4AZs extends AVPCTest {

    @Test
    public void test() {
        final String stackName = "vpc-4azs-" + this.random8String();
        try {
            this.createStack(stackName,
                    "vpc/vpc-4azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue("10")
            );
            this.testVPCSubnetInternetAccess(stackName, "SubnetAPublic");
            this.testVPCSubnetInternetAccess(stackName, "SubnetBPublic");
            this.testVPCSubnetInternetAccess(stackName, "SubnetCPublic");
            this.testVPCSubnetInternetAccess(stackName, "SubnetDPublic");
        } finally {
            this.deleteStack(stackName);
        }
    }

}
