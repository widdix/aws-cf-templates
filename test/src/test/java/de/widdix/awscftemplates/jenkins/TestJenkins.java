package de.widdix.awscftemplates.jenkins;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.taimos.httputils.WS;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Context;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestJenkins extends ACloudFormationTest {

    @Test
    public void testHA() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "jenkins-ha-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String masterAdminPassword = this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, stackName,
                            "jenkins/jenkins2-ha.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("MasterInstanceType").withParameterValue("t3a.small"),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName),
                            new Parameter().withParameterKey("MasterAdminPassword").withParameterValue(masterAdminPassword),
                            new Parameter().withParameterKey("EFSBackupRetentionPeriod").withParameterValue("0")
                    );
                    final String url = this.getStackOutputValue(stackName, "URL");
                    final Callable<String> callable = () -> {
                        final HttpResponse response = WS.url(url).authBasic("admin", masterAdminPassword).timeout(10000).get();
                        // check HTTP response code
                        if (WS.getStatus(response) != 200) {
                            throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                        }
                        return WS.getResponseAsString(response);
                    };
                    final String response = this.retry(context, callable);
                    // check if Jenkins appears in HTML
                    Assert.assertTrue("http response body contains \"Jenkins\"", response.contains("Jenkins"));
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
    public void testHAAgents() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "jenkins-ha-agents-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String masterAdminPassword = this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, stackName,
                            "jenkins/jenkins2-ha-agents.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("MasterInstanceType").withParameterValue("t3a.small"),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName),
                            new Parameter().withParameterKey("MasterAdminPassword").withParameterValue(masterAdminPassword),
                            new Parameter().withParameterKey("EFSBackupRetentionPeriod").withParameterValue("0")
                    );
                    final String url = this.getStackOutputValue(stackName, "URL");
                    final Callable<String> callable = () -> {
                        final HttpResponse response = WS.url(url).authBasic("admin", masterAdminPassword).timeout(10000).get();
                        // check HTTP response code
                        if (WS.getStatus(response) != 200) {
                            throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                        }
                        return WS.getResponseAsString(response);
                    };
                    final String response = this.retry(context, callable);
                    // check if Jenkins appears in HTML
                    Assert.assertTrue("http response body contains \"Jenkins\"", response.contains("Jenkins"));
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

}
