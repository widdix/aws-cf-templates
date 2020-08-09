package de.widdix.awscftemplates.ec2;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestAL2MutablePrivate extends ACloudFormationTest {

    @Test
    public void test() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String natStackName = "vpc-nat-gateway-" + this.random8String();
        final String stackName = "al2-mutable-private-" + this.random8String();
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
                try {
                    this.createStack(stackName,
                            "ec2/al2-mutable-private.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("BackupRetentionPeriod").withParameterValue("0")
                    );
                    // TODO how can we check if this stack works?
                } finally {
                    this.deleteStack(stackName);
                }
            } finally {
                this.deleteStack(natStackName);
            }
        } finally {
            this.deleteStack(vpcStackName);
        }
    }

}
