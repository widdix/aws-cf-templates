package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import org.junit.Test;

import java.util.Map;

public class TestVPC4AZsLegacy extends AVPCTest {

    @Test
    public void test() {
        final String vpcStackName = "vpc-4azs-" + this.random8String();
        final String stackName = "vpc-4azs-legacy-" + this.random8String();
        try {
            this.createStack(vpcStackName,
                    "vpc/vpc-4azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue("10")
            );
            final Map<String, String> vpcOutputs = this.getStackOutputs(vpcStackName);
            try {
                this.createStack(stackName,
                        "vpc/vpc-4azs-legacy.yaml",
                        new Parameter().withParameterKey("AZA").withParameterValue(vpcOutputs.get("AZA")),
                        new Parameter().withParameterKey("AZB").withParameterValue(vpcOutputs.get("AZB")),
                        new Parameter().withParameterKey("AZC").withParameterValue(vpcOutputs.get("AZC")),
                        new Parameter().withParameterKey("AZD").withParameterValue(vpcOutputs.get("AZD")),
                        new Parameter().withParameterKey("ClassB").withParameterValue(vpcOutputs.get("ClassB")),
                        new Parameter().withParameterKey("VPC").withParameterValue(vpcOutputs.get("VPC")),
                        new Parameter().withParameterKey("SubnetAPublic").withParameterValue(vpcOutputs.get("SubnetAPublic")),
                        new Parameter().withParameterKey("RouteTableAPublic").withParameterValue(vpcOutputs.get("RouteTableAPublic")),
                        new Parameter().withParameterKey("SubnetBPublic").withParameterValue(vpcOutputs.get("SubnetBPublic")),
                        new Parameter().withParameterKey("RouteTableBPublic").withParameterValue(vpcOutputs.get("RouteTableBPublic")),
                        new Parameter().withParameterKey("SubnetCPublic").withParameterValue(vpcOutputs.get("SubnetCPublic")),
                        new Parameter().withParameterKey("RouteTableCPublic").withParameterValue(vpcOutputs.get("RouteTableCPublic")),
                        new Parameter().withParameterKey("SubnetDPublic").withParameterValue(vpcOutputs.get("SubnetDPublic")),
                        new Parameter().withParameterKey("RouteTableDPublic").withParameterValue(vpcOutputs.get("RouteTableDPublic")),
                        new Parameter().withParameterKey("SubnetAPrivate").withParameterValue(vpcOutputs.get("SubnetAPrivate")),
                        new Parameter().withParameterKey("RouteTableAPrivate").withParameterValue(vpcOutputs.get("RouteTableAPrivate")),
                        new Parameter().withParameterKey("SubnetBPrivate").withParameterValue(vpcOutputs.get("SubnetBPrivate")),
                        new Parameter().withParameterKey("RouteTableBPrivate").withParameterValue(vpcOutputs.get("RouteTableBPrivate")),
                        new Parameter().withParameterKey("SubnetCPrivate").withParameterValue(vpcOutputs.get("SubnetCPrivate")),
                        new Parameter().withParameterKey("RouteTableCPrivate").withParameterValue(vpcOutputs.get("RouteTableCPrivate")),
                        new Parameter().withParameterKey("SubnetDPrivate").withParameterValue(vpcOutputs.get("SubnetDPrivate")),
                        new Parameter().withParameterKey("RouteTableDPrivate").withParameterValue(vpcOutputs.get("RouteTableDPrivate"))
                );
                this.testVPCSubnetInternetAccess(stackName, "SubnetAPublic");
                this.testVPCSubnetInternetAccess(stackName, "SubnetBPublic");
                this.testVPCSubnetInternetAccess(stackName, "SubnetCPublic");
                this.testVPCSubnetInternetAccess(stackName, "SubnetDPublic");
            } finally {
                this.deleteStack(stackName);
            }
        } finally {
            this.deleteStack(vpcStackName);
        }
    }

}
