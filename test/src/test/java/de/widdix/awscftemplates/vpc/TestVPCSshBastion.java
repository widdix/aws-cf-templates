package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.ec2.model.KeyPair;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestVPCSshBastion extends ACloudFormationTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String bastionStackName = "vpc-ssh-bastion-" + this.random8String();
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
                    this.createStack(context, bastionStackName,
                            "vpc/vpc-ssh-bastion.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    final String host = this.getStackOutputValue(bastionStackName, "IPAddress");
                    this.probeSSH(context, host, key);
                } finally {
                    this.deleteStack(context, bastionStackName);
                }
            } finally {
                this.deleteStack(context, vpcStackName);
            }
        } finally {
            this.deleteKey(context, keyName);
        }
    }

}
