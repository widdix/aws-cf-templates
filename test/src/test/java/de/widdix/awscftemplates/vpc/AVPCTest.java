package de.widdix.awscftemplates.vpc;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;

public abstract class AVPCTest extends ACloudFormationTest {

    protected final void testVPCSubnetInternetAccess(final Context context, final String parentVPCStack, final String subnetName) {
        final String stackName = "al2-mutable-public-" + this.random8String();
        try {
            this.createStack(context, stackName,
                    "ec2/al2-mutable-public.yaml",
                    new Parameter().withParameterKey("ParentVPCStack").withParameterValue(parentVPCStack),
                    new Parameter().withParameterKey("SubnetName").withParameterValue(subnetName)
            );
        } finally {
            this.deleteStack(context, stackName);
        }
    }

}
