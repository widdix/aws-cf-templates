package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import org.junit.Test;

import java.util.Map;

public class TestVPC2AZsLegacy extends AVPCTest {

    @Test
    public void test() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "vpc-2azs-legacy-" + this.random8String();
        try {
            this.createStack(vpcStackName,
                    "vpc/vpc-2azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue("10")
            );
            final Map<String, String> vpcOutputs = this.getStackOutputs(vpcStackName);
            try {
                this.createStack(stackName,
                        "vpc/vpc-2azs-legacy.yaml",
                        new Parameter().withParameterKey("AZA").withParameterValue(vpcOutputs.get("AZA")),
                        new Parameter().withParameterKey("AZB").withParameterValue(vpcOutputs.get("AZB")),
                        new Parameter().withParameterKey("CidrBlock").withParameterValue(vpcOutputs.get("CidrBlock")),
                        new Parameter().withParameterKey("VPC").withParameterValue(vpcOutputs.get("VPC")),
                        new Parameter().withParameterKey("InternetGateway").withParameterValue(vpcOutputs.get("InternetGateway")),
                        new Parameter().withParameterKey("SubnetAPublic").withParameterValue(vpcOutputs.get("SubnetAPublic")),
                        new Parameter().withParameterKey("RouteTableAPublic").withParameterValue(vpcOutputs.get("RouteTableAPublic")),
                        new Parameter().withParameterKey("SubnetBPublic").withParameterValue(vpcOutputs.get("SubnetBPublic")),
                        new Parameter().withParameterKey("RouteTableBPublic").withParameterValue(vpcOutputs.get("RouteTableBPublic")),
                        new Parameter().withParameterKey("SubnetAPrivate").withParameterValue(vpcOutputs.get("SubnetAPrivate")),
                        new Parameter().withParameterKey("RouteTableAPrivate").withParameterValue(vpcOutputs.get("RouteTableAPrivate")),
                        new Parameter().withParameterKey("SubnetBPrivate").withParameterValue(vpcOutputs.get("SubnetBPrivate")),
                        new Parameter().withParameterKey("RouteTableBPrivate").withParameterValue(vpcOutputs.get("RouteTableBPrivate"))
                );
                this.testVPCSubnetInternetAccess(stackName, "SubnetAPublic");
                this.testVPCSubnetInternetAccess(stackName, "SubnetBPublic");
            } finally {
                this.deleteStack(stackName);
            }
        } finally {
            this.deleteStack(vpcStackName);
        }
    }

}
