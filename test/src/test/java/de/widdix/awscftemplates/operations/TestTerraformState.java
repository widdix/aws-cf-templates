package de.widdix.awscftemplates.operations;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestTerraformState extends ACloudFormationTest {

    @Test
    public void test() {
        final String kmsStackName = "kms-" + this.random8String();
        final String terraformStateStackName = "tf-state-" + this.random8String();
        try {
            this.createStack(kmsStackName,
                    "security/kms-key.yaml",
                    new Parameter().withParameterKey("Service").withParameterValue("s3")
            );
            try {
                this.createStack(terraformStateStackName,
                        "operations/terraform-state.yaml",
                        new Parameter().withParameterKey("ParentKmsKeyStack").withParameterValue(kmsStackName),
                        new Parameter().withParameterKey("TerraformStateIdentifier").withParameterValue(terraformStateStackName),
                        new Parameter().withParameterKey("TerraformStateAdminARNs").withParameterValue("arn:aws:iam::" + this.getAccount() + ":root," + System.getenv("IAM_ROLE_ARN") + "," + this.getCallerIdentityArn())
                );
            } finally {
                this.deleteStack(terraformStateStackName);
            }
        } finally {
            this.deleteStack(kmsStackName);
        }
    }

}
