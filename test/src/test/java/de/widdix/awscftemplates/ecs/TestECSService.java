package de.widdix.awscftemplates.ecs;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.taimos.httputils.WS;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Config;
import de.widdix.awscftemplates.Context;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestECSService extends ACloudFormationTest {

    @Test
    public void testClusterAlbHostPattern() {
        final Context context = new Context();
        final String zoneStackName = "zone-" + this.random8String();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String subDomainName = stackName;
        try {
            this.createKey(keyName);
            try {
                this.createStack(context, zoneStackName,
                        "vpc/zone-legacy.yaml",
                        new Parameter().withParameterKey("HostedZoneName").withParameterValue(Config.get(Config.Key.DOMAIN_SUFFIX)),
                        new Parameter().withParameterKey("HostedZoneId").withParameterValue(Config.get(Config.Key.HOSTED_ZONE_ID))
                );
                try {
                    this.createStack(context, vpcStackName,
                            "vpc/vpc-2azs.yaml",
                            new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                    );
                    try {
                        this.createStack(context, clusterStackName,
                                "ecs/cluster.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                        );
                        try {
                            final String domain = subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                            this.createStack(context, stackName,
                                    "ecs/service-cluster-alb.yaml",
                                    new Parameter().withParameterKey("ParentClusterStack").withParameterValue(clusterStackName),
                                    new Parameter().withParameterKey("ParentZoneStack").withParameterValue(zoneStackName),
                                    new Parameter().withParameterKey("SubDomainNameWithDot").withParameterValue(subDomainName + "."),
                                    new Parameter().withParameterKey("Image").withParameterValue("nginx:1.11.5"),
                                    new Parameter().withParameterKey("LoadBalancerPath").withParameterValue(""),
                                    new Parameter().withParameterKey("LoadBalancerHostPattern").withParameterValue(domain)
                            );
                            final String url = "http://" + domain;
                            final Callable<String> callable = () -> {
                                final HttpResponse response = WS.url(url).timeout(10000).get();
                                // check HTTP response code
                                if (WS.getStatus(response) != 200) {
                                    throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                                }
                                return WS.getResponseAsString(response);
                            };
                            final String response = this.retry(context, callable);
                            // check if nginx page appears
                            Assert.assertTrue("http response body contains \"Welcome to nginx!\"", response.contains("Welcome to nginx!"));
                        } finally {
                            this.deleteStack(context, stackName);
                        }
                    } finally {
                        this.deleteStack(context, clusterStackName);
                    }
                } finally {
                    this.deleteStack(context, vpcStackName);
                }
            } finally {
                this.deleteStack(context, zoneStackName);
            }
        } finally {
            this.deleteKey(context, keyName);
        }
    }

    @Test
    public void testClusterAlbPathPattern() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, clusterStackName,
                            "ecs/cluster.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    final String cluster = this.getStackOutputValue(clusterStackName, "Cluster");
                    try {
                        this.createStack(context, stackName,
                                "ecs/service-cluster-alb.yaml",
                                new Parameter().withParameterKey("ParentClusterStack").withParameterValue(clusterStackName),
                                new Parameter().withParameterKey("Image").withParameterValue("nginx:1.11.5")
                        );
                        final String url = this.getStackOutputValue(stackName, "URL");
                        final Callable<Boolean> callable = () -> {
                            final HttpResponse response = WS.url(url).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 404) {
                                throw new RuntimeException("404 expected, but saw " + WS.getStatus(response));
                            }
                            return true;
                        };
                        Assert.assertTrue("http response code is 404", this.retry(context, callable));
                    } finally {
                        this.deleteStack(context, stackName);
                    }
                } finally {
                    this.deleteStack(context, clusterStackName);
                }
            } finally {
                this.deleteStack(context, vpcStackName);
            }
        } finally {
            this.deleteKey(context, keyName);
        }
    }


    @Test
    public void testDedicatedAlb() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, clusterStackName,
                            "ecs/cluster.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    try {
                        this.createStack(context, stackName,
                                "ecs/service-dedicated-alb.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("ParentClusterStack").withParameterValue(clusterStackName),
                                new Parameter().withParameterKey("Image").withParameterValue("nginx:1.11.5")
                        );
                        final String url = this.getStackOutputValue(stackName, "URL");
                        final Callable<String> callable = () -> {
                            final HttpResponse response = WS.url(url).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 200) {
                                throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                            }
                            return WS.getResponseAsString(response);
                        };
                        final String response = this.retry(context, callable);
                        // check if nginx page appears
                        Assert.assertTrue("http response body contains \"Welcome to nginx!\"", response.contains("Welcome to nginx!"));
                    } finally {
                        this.deleteStack(context, stackName);
                    }
                } finally {
                    this.deleteStack(context, clusterStackName);
                }
            } finally {
                this.deleteStack(context, vpcStackName);
            }
        } finally {
            this.deleteKey(context, keyName);
        }
    }

    @Test
    public void testWaf() {
        final Context context = new Context();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String wafStackName = "waf-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(context, vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(context, wafStackName, "security/waf.yaml");
                    try {
                        this.createStack(context, clusterStackName,
                                "ecs/cluster.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("ParentWAFStack").withParameterValue(wafStackName),
                                new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                        );
                        try {
                            this.createStack(context, stackName,
                                    "ecs/service-dedicated-alb.yaml",
                                    new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                    new Parameter().withParameterKey("ParentWAFStack").withParameterValue(wafStackName),
                                    new Parameter().withParameterKey("ParentClusterStack").withParameterValue(clusterStackName),
                                    new Parameter().withParameterKey("Image").withParameterValue("nginx:1.11.5")
                            );
                            final String url = this.getStackOutputValue(stackName, "URL");
                            final Callable<String> callable = () -> {
                                final HttpResponse response = WS.url(url).timeout(10000).get();
                                // check HTTP response code
                                if (WS.getStatus(response) != 200) {
                                    throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                                }
                                return WS.getResponseAsString(response);
                            };
                            final String response = this.retry(context, callable);
                            // check if nginx page appears
                            Assert.assertTrue("http response body contains \"Welcome to nginx!\"", response.contains("Welcome to nginx!"));
                        } finally {
                            this.deleteStack(context, stackName);
                        }
                    } finally {
                        this.deleteStack(context, clusterStackName);
                    }
                } finally {
                    this.deleteStack(context, wafStackName);
                }
            } finally {
                this.deleteStack(context, vpcStackName);
            }
        } finally {
            this.deleteKey(context, keyName);
        }
    }

}
