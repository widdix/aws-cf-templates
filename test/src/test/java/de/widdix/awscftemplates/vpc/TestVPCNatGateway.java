package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import org.junit.Test;

public class TestVPCNatGateway extends AVPCTest {

    @Test
    public void test() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String natStackName = "vpc-nat-gateway-" + this.random8String();
        final String classB = "10";
        try {
            this.createStack(vpcStackName,
                    "vpc/vpc-2azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue(classB)
            );
            try {
                this.createStack(natStackName,
                        "vpc/vpc-nat-gateway.yaml",
                        new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName)
                );
                this.testVPCSubnetInternetAccess(vpcStackName, "SubnetAPrivate");
            } finally {
                this.deleteStack(natStackName);
            }
        } finally {
            this.deleteStack(vpcStackName);
        }
    }

}
