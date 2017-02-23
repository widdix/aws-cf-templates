package de.widdix.awscftemplates.ec2;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.ec2.model.KeyPair;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestEC2AutoRecovery extends ACloudFormationTest {

    @Test
    public void test() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "ec2-auto-recovery-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            final KeyPair key = this.createKey(keyName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                this.waitForStack(vpcStackName, FinalStatus.CREATE_COMPLETE);
                try {
                    this.createStack(stackName,
                            "ec2/ec2-auto-recovery.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    this.waitForStack(stackName, FinalStatus.CREATE_COMPLETE);
                    final String host = this.getStackOutputValue(stackName, "IPAddress");
                    this.probeSSH(host, key);
                } finally {
                    this.deleteStack(stackName);
                    this.waitForStack(stackName, FinalStatus.DELETE_COMPLETE);
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
