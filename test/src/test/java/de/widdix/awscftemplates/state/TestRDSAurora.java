package de.widdix.awscftemplates.state;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRDSAurora extends ACloudFormationTest {

    @Test
    public void testMyQSLVersion57() {
        this.testVersion("5.7.mysql-aurora.2.11.1");
    }

    @Test
    public void testMySQLVersion8() {
        this.testVersion("8.0.mysql-aurora.3.02.1", "db.t3.medium");
    }

    @Test
    public void testPostgresVersion14() {
        this.testVersion("aurora-postgresql-14.6", "db.t3.medium");
    }

    private void testVersion(final String version) {
        this.testVersion(version, null);
    }

    private void testVersion(final String version, final String instanceClassOrNull) {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clientStackName = "client-" + this.random8String();
        final String stackName = "rds-aurora-" + this.random8String();
        final String password = this.random8String();
        try {
            this.createStack(context, vpcStackName, "vpc/vpc-2azs.yaml");
            try {
                this.createStack(context, clientStackName,
                        "state/client-sg.yaml",
                        new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName)
                );
                try {
                    final List<Parameter> parameters= new ArrayList<>(Arrays.asList(
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("ParentClientStack").withParameterValue(clientStackName),
                            new Parameter().withParameterKey("DBName").withParameterValue("db1"),
                            new Parameter().withParameterKey("DBMasterUserPassword").withParameterValue(password),
                            new Parameter().withParameterKey("Engine").withParameterValue(version)
                    ));
                    if (instanceClassOrNull != null) {
                        parameters.add(new Parameter().withParameterKey("DBInstanceClass").withParameterValue(instanceClassOrNull));
                    }
                    this.createStack(context, stackName,
                            "state/rds-aurora.yaml",
                            parameters.toArray(new Parameter[0])
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


    @Test
    public void testWithSecret() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clientStackName = "client-" + this.random8String();
        final String secretStackName = "secret-" + this.random8String();
        final String stackName = "rds-aurora-" + this.random8String();
        try {
            this.createStack(context, vpcStackName, "vpc/vpc-2azs.yaml");
            try {
                this.createStack(context, clientStackName,
                        "state/client-sg.yaml",
                        new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName)
                );
                try {
                    this.createStack(context, secretStackName,"state/secretsmanager-dbsecret.yaml");
                    try {
                        this.createStack(context, stackName,
                                "state/rds-aurora.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("ParentClientStack").withParameterValue(clientStackName),
                                new Parameter().withParameterKey("ParentSecretStack").withParameterValue(secretStackName),
                                new Parameter().withParameterKey("DBName").withParameterValue("db1"),
                                new Parameter().withParameterKey("Engine").withParameterValue("5.7.mysql-aurora.2.11.1")
                        );
                        // TODO how can we check if this stack works? start a bastion host and try to connect?
                    } finally {
                        this.deleteStack(context, stackName);
                    }
                } finally {
                    this.deleteStack(context, secretStackName);
                }
            } finally {
                this.deleteStack(context, clientStackName);
            }
        } finally {
            this.deleteStack(context, vpcStackName);
        }
    }

}
