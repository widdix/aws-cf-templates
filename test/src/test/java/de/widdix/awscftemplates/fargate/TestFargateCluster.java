package de.widdix.awscftemplates.fargate;

import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestFargateCluster extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "fargate-cluster-" + this.random8String();
        try {
            this.createStack(stackName,
                    "fargate/cluster.yaml"
            );
        } finally {
            this.deleteStack(stackName);
        }
    }

}
