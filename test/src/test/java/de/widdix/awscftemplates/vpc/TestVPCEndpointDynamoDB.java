package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestVPCEndpointDynamoDB extends ACloudFormationTest {
    @Test
    public void test() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String endpointStackName = "vpc-endpoint-dynamodb-" + this.random8String();
        final String classB = "10";
        try {
            this.createStack(vpcStackName,
                    "vpc/vpc-2azs.yaml",
                    new Parameter().withParameterKey("ClassB").withParameterValue(classB)
            );
            try {
                this.createStack(endpointStackName,
                        "vpc/vpc-endpoint-dynamodb.yaml",
                        new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName)
                );
                // TODO how can we check if this stack works?
            } finally {
                this.deleteStack(endpointStackName);
            }
        } finally {
            this.deleteStack(vpcStackName);
        }
    }

}
