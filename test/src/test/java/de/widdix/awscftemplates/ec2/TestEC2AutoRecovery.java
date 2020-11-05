package de.widdix.awscftemplates.ec2;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.ec2.model.KeyPair;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestEC2AutoRecovery extends ACloudFormationTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "ec2-auto-recovery-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            final KeyPair key = this.createKey(keyName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, stackName,
                            "ec2/ec2-auto-recovery.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    final String host = this.getStackOutputValue(stackName, "IPAddress");
                    this.probeSSH(context, host, key);
                } finally {
                    this.deleteStack(context, stackName);
                }
            } finally {
                this.deleteStack(context, vpcStackName);
            }
        } finally {
            this.deleteKey(context, keyName);
        }
    }

    @Test
    public void testWithIAMUserSSHAccess() throws Exception {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "ec2-auto-recovery-" + this.random8String();
        final String classB = "10";
        final String userName = "user-" + this.random8String();
        try {
            final User user = this.createUser(userName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, stackName,
                            "ec2/ec2-auto-recovery.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("IAMUserSSHAccess").withParameterValue("true")
                    );
                    final String host = this.getStackOutputValue(stackName, "IPAddress");
                    this.probeSSH(context, host, user);
                } finally {
                    this.deleteStack(context, stackName);
                }
            } finally {
                this.deleteStack(context, vpcStackName);
            }
        } finally {
            this.deleteUser(context, userName);
        }
    }
}
