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
                try {
                    this.createStack(stackName,
                            "ec2/ec2-auto-recovery.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    final String host = this.getStackOutputValue(stackName, "IPAddress");
                    this.probeSSH(host, key);
                } finally {
                    this.deleteStack(stackName);
                }
            } finally {
                this.deleteStack(vpcStackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

    @Test
    public void testWithIAMUserSSHAccess() throws Exception {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "ec2-auto-recovery-" + this.random8String();
        final String classB = "10";
        final String userName = "user-" + this.random8String();
        try {
            final User user = this.createUser(userName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(stackName,
                            "ec2/ec2-auto-recovery.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("IAMUserSSHAccess").withParameterValue("true")
                    );
                    final String host = this.getStackOutputValue(stackName, "IPAddress");
                    this.probeSSH(host, user);
                } finally {
                    this.deleteStack(stackName);
                }
            } finally {
                this.deleteStack(vpcStackName);
            }
        } finally {
            this.deleteUser(userName);
        }
    }
}
