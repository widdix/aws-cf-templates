package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.ec2.model.KeyPair;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestVPCVpnBastion extends ACloudFormationTest {

    @Test
    public void test() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String bastionStackName = "vpc-vpn-bastion-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String vpnPSK = this.random8String();
        final String vpnUserPassword = this.random8String();
        final String vpnAdminPassword = this.random8String();
        try {
            final KeyPair key = this.createKey(keyName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, bastionStackName,
                            "vpc/vpc-vpn-bastion.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName),
                            new Parameter().withParameterKey("VPNPSK").withParameterValue(vpnPSK),
                            new Parameter().withParameterKey("VPNUserName").withParameterValue("test"),
                            new Parameter().withParameterKey("VPNUserPassword").withParameterValue(vpnUserPassword),
                            new Parameter().withParameterKey("VPNAdminPassword").withParameterValue(vpnAdminPassword),
                            new Parameter().withParameterKey("EFSBackupRetentionPeriod").withParameterValue("0")
                    );
                    // TODO how can we check if this stack works?
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
