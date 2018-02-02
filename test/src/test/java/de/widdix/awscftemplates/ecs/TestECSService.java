package de.widdix.awscftemplates.ecs;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.taimos.httputils.WS;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Config;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestECSService extends ACloudFormationTest {

    @Test
    public void testClusterAlbHostPattern() {
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
                this.createStack(zoneStackName,
                        "vpc/zone-legacy.yaml",
                        new Parameter().withParameterKey("HostedZoneName").withParameterValue(Config.get(Config.Key.DOMAIN_SUFFIX)),
                        new Parameter().withParameterKey("HostedZoneId").withParameterValue(Config.get(Config.Key.HOSTED_ZONE_ID))
                );
                try {
                    this.createStack(vpcStackName,
                            "vpc/vpc-2azs.yaml",
                            new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                    );
                    try {
                        this.createStack(clusterStackName,
                                "ecs/cluster.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                        );
                        try {
                            final String domain = subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                            this.createStack(stackName,
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
                            final String response = this.retry(callable);
                            // check if nginx page appears
                            Assert.assertTrue(response.contains("Welcome to nginx!"));
                        } finally {
                            this.deleteStack(stackName);
                        }
                    } finally {
                        this.deleteStack(clusterStackName);
                    }
                } finally {
                    this.deleteStack(vpcStackName);
                }
            } finally {
                this.deleteStack(zoneStackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

    @Test
    public void testClusterAlbPathPattern() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(clusterStackName,
                            "ecs/cluster.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    final String cluster = this.getStackOutputValue(clusterStackName, "Cluster");
                    try {
                        this.createStack(stackName,
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
                        Assert.assertTrue(this.retry(callable));
                    } finally {
                        this.deleteStack(stackName);
                    }
                } finally {
                    this.deleteStack(clusterStackName);
                }
            } finally {
                this.deleteStack(vpcStackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }


    @Test
    public void testDedicatedAlb() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String clusterStackName = "ecs-cluster-" + this.random8String();
        final String stackName = "ecs-service-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(clusterStackName,
                            "ecs/cluster.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    try {
                        this.createStack(stackName,
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
                        final String response = this.retry(callable);
                        // check if nginx page appears
                        Assert.assertTrue(response.contains("Welcome to nginx!"));
                    } finally {
                        this.deleteStack(stackName);
                    }
                } finally {
                    this.deleteStack(clusterStackName);
                }
            } finally {
                this.deleteStack(vpcStackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

}
