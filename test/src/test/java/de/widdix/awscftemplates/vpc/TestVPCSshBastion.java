package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.ec2.model.KeyPair;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestVPCSshBastion extends ACloudFormationTest {

    @Test
    public void test() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String bastionStackName = "vpc-ssh-bastion-" + this.random8String();
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
                    this.createStack(bastionStackName,
                            "vpc/vpc-ssh-bastion.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    this.waitForStack(bastionStackName, FinalStatus.CREATE_COMPLETE);
                    final String host = this.getStackOutputValue(bastionStackName, "IPAddress");
                    this.probeSSH(host, key);
                } finally {
                    this.deleteStack(bastionStackName);
                    this.waitForStack(bastionStackName, FinalStatus.DELETE_COMPLETE);
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
