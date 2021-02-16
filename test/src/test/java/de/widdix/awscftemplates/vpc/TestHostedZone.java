package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import de.widdix.awscftemplates.Config;
import org.junit.Test;

public class TestHostedZone extends ACloudFormationTest {

    @Test
    public void testDNSSEC() {
        final Context context = new Context();
        final String keyStackName = "key-" + this.random8String();
        final String zoneStackName = "hostedzone-" + this.random8String();
        final String dnssecStackName = "dnssec-" + this.random8String();
        try {
            this.createStack(context, keyStackName,
                    "security/kms-key.yaml",
                    new Parameter().withParameterKey("Service").withParameterValue("dnssec-route53"),
                    new Parameter().withParameterKey("KeySpec").withParameterValue("ECC_NIST_P256"),
                    new Parameter().withParameterKey("KeyUsage").withParameterValue("SIGN_VERIFY")
            );
            try {
                this.createStack(context, zoneStackName,
                        "vpc/zone-legacy.yaml",
                        new Parameter().withParameterKey("HostedZoneName").withParameterValue(Config.get(Config.Key.DOMAIN_SUFFIX)),
                        new Parameter().withParameterKey("HostedZoneId").withParameterValue(Config.get(Config.Key.HOSTED_ZONE_ID))
                );
                try {
                    this.createStack(context, dnssecStackName,
                            "vpc/zone-dnssec.yaml",
                            new Parameter().withParameterKey("ParentZoneStack").withParameterValue(zoneStackName),
                            new Parameter().withParameterKey("ParentKmsKeyStack").withParameterValue(keyStackName)
                    );
                } finally {
                    this.deleteStack(context, dnssecStackName);
                }
            } finally {
                this.deleteStack(context, zoneStackName);
            }
        } finally {
            this.deleteStack(context, keyStackName);
        }
    }

}
