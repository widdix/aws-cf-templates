package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestVPC2AZs extends AVPCTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String stackName = "vpc-2azs-" + this.random8String();
        try {
            this.createStack(context, stackName,
                    "vpc/vpc-2azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue("10")
            );
            this.testVPCSubnetInternetAccess(context, stackName, "SubnetAPublic");
            this.testVPCSubnetInternetAccess(context, stackName, "SubnetBPublic");
        } finally {
            this.deleteStack(context, stackName);
        }
    }

}
