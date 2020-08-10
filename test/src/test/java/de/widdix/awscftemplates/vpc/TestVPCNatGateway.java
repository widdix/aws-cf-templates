package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestVPCNatGateway extends AVPCTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String natStackName = "vpc-nat-gateway-" + this.random8String();
        final String classB = "10";
        try {
            this.createStack(context, vpcStackName,
                    "vpc/vpc-2azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue(classB)
            );
            try {
                this.createStack(context, natStackName,
                        "vpc/vpc-nat-gateway.yaml",
                        new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName)
                );
                this.testVPCSubnetInternetAccess(context, vpcStackName, "SubnetAPrivate");
            } finally {
                this.deleteStack(context, natStackName);
            }
        } finally {
            this.deleteStack(context, vpcStackName);
        }
    }

}
