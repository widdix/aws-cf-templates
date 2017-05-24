package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;

public abstract class AVPCTest extends ACloudFormationTest {

    protected final void testVPCSubnetInternetAccess(final String parentVPCStack, final String subnetName) {
        final String stackName = "ec2-auto-recovery-" + this.random8String();
        try {
            this.createStack(stackName,
                    "ec2/ec2-auto-recovery.yaml",
                    new Parameter().withParameterKey("ParentVPCStack").withParameterValue(parentVPCStack),
                    new Parameter().withParameterKey("SubnetName").withParameterValue(subnetName)
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
