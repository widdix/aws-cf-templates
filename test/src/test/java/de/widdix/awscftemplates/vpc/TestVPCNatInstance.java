package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestVPCNatInstance extends ACloudFormationTest {

    @Test
    public void test() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String natStackName = "vpc-nat-instance-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                this.waitForStack(vpcStackName, FinalStatus.CREATE_COMPLETE);
                try {
                    this.createStack(natStackName,
                            "vpc/vpc-nat-instance.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    this.waitForStack(natStackName, FinalStatus.CREATE_COMPLETE);
                    // TODO how can we check if this stack works?  launch an EC2 instance into a private subnet and open google from the instance?
                } finally {
                    this.deleteStack(natStackName);
                    this.waitForStack(natStackName, FinalStatus.DELETE_COMPLETE);
                }
            } finally {
                this.deleteStack(vpcStackName);
                this.waitForStack(vpcStackName, FinalStatus.DELETE_COMPLETE);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

}
