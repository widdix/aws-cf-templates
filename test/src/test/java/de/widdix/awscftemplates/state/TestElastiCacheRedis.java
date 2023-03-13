package de.widdix.awscftemplates.state;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

public class TestElastiCacheRedis extends ACloudFormationTest {

    @Test
    public void testVersion3() {
        this.testVersion("3.2.6");
    }

    @Test
    public void testVersion4() {
        this.testVersion("4.0.10");
    }

    @Test
    public void testVersion5() {
        this.testVersion("5.0.6");
    }

    @Test
    public void testVersion6() {
        this.testVersion("6.2");
    }

    @Test
    public void testVersion7() {
        this.testVersion("7.0");
    }

    private void testVersion(final String version) {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clientStackName = "client-" + this.random8String();
        final String stackName = "elasticache-redis-" + this.random8String();
        try {
            this.createStack(context, vpcStackName, "vpc/vpc-2azs.yaml");
            try {
                this.createStack(context, clientStackName,
                        "state/client-sg.yaml",
                        new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName)
                );
                try {
                    this.createStack(context, stackName,
                            "state/elasticache-redis.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("ParentClientStack").withParameterValue(clientStackName),
                            new Parameter().withParameterKey("EngineVersion").withParameterValue(version)
                    );
                    // TODO how can we check if this stack works? start a bastion host and try to connect?
                } finally {
                    this.deleteStack(context, stackName);
                }
            } finally {
                this.deleteStack(context, clientStackName);
            }
        } finally {
            this.deleteStack(context, vpcStackName);
        }
    }

}
