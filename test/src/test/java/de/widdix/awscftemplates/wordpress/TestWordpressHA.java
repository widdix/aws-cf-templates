package de.widdix.awscftemplates.wordpress;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.taimos.httputils.WS;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Config;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestWordpressHA extends ACloudFormationTest {

    @Test
    public void testMySQL() {
        final String zoneStackName = "zone-" + this.random8String();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "wordpress-ha-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String subDomainName = stackName;
        final String blogTitle = "Stay-AWSome";
        final String blogPassword = this.random8String();
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
                        this.createStack(stackName,
                                "wordpress/wordpress-ha.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("ParentZoneStack").withParameterValue(zoneStackName),
                                new Parameter().withParameterKey("WebServerKeyName").withParameterValue(keyName),
                                new Parameter().withParameterKey("SubDomainNameWithDot").withParameterValue(subDomainName + "."),
                                new Parameter().withParameterKey("CloudFrontAcmCertificate").withParameterValue(Config.get(Config.Key.CLOUDFRONT_ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("ElbAcmCertificate").withParameterValue(Config.get(Config.Key.ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("BlogTitle").withParameterValue(blogTitle),
                                new Parameter().withParameterKey("BlogAdminUsername").withParameterValue("admin"),
                                new Parameter().withParameterKey("BlogAdminPassword").withParameterValue(blogPassword),
                                new Parameter().withParameterKey("BlogAdminEMail").withParameterValue("no-reply@widdix.de"),
                                new Parameter().withParameterKey("EFSBackupRetentionPeriod").withParameterValue("0")
                        );
                        final String url = "https://" + subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                        final Callable<String> callable = () -> {
                            final HttpResponse response = WS.url(url).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 200) {
                                throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                            }
                            return WS.getResponseAsString(response);
                        };
                        final String response = this.retry(callable);
                        // check if blog title appears in HTML
                        Assert.assertTrue("http response body contains \"" + blogTitle + "\"", response.contains(blogTitle));
                    } finally {
                        this.deleteStackAndRetryOnFailure(stackName);
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
    public void testMySQLWithEFSProvisionedThroughput() {
        final String zoneStackName = "zone-" + this.random8String();
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "wordpress-ha-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String subDomainName = stackName;
        final String blogTitle = "Stay-AWSome";
        final String blogPassword = this.random8String();
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
                        this.createStack(stackName,
                                "wordpress/wordpress-ha.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("ParentZoneStack").withParameterValue(zoneStackName),
                                new Parameter().withParameterKey("WebServerKeyName").withParameterValue(keyName),
                                new Parameter().withParameterKey("SubDomainNameWithDot").withParameterValue(subDomainName + "."),
                                new Parameter().withParameterKey("CloudFrontAcmCertificate").withParameterValue(Config.get(Config.Key.CLOUDFRONT_ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("ElbAcmCertificate").withParameterValue(Config.get(Config.Key.ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("BlogTitle").withParameterValue(blogTitle),
                                new Parameter().withParameterKey("BlogAdminUsername").withParameterValue("admin"),
                                new Parameter().withParameterKey("BlogAdminPassword").withParameterValue(blogPassword),
                                new Parameter().withParameterKey("BlogAdminEMail").withParameterValue("no-reply@widdix.de"),
                                new Parameter().withParameterKey("EFSProvisionedThroughputInMibps").withParameterValue("1"),
                                new Parameter().withParameterKey("EFSBackupRetentionPeriod").withParameterValue("0")
                        );
                        final String url = "https://" + subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                        final Callable<String> callable = () -> {
                            final HttpResponse response = WS.url(url).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 200) {
                                throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                            }
                            return WS.getResponseAsString(response);
                        };
                        final String response = this.retry(callable);
                        // check if blog title appears in HTML
                        Assert.assertTrue("http response body contains \"" + blogTitle + "\"", response.contains(blogTitle));
                    } finally {
                        this.deleteStackAndRetryOnFailure(stackName);
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
    public void testAurora() {
        final String zoneStackName = "zone-" + this.random8String();
        final String vpcStackName = "vpc-3azs-" + this.random8String();
        final String stackName = "wordpress-ha-aurora-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String subDomainName = stackName;
        final String blogTitle = "Stay-AWSome";
        final String blogPassword = this.random8String();
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
                            "vpc/vpc-3azs.yaml",
                            new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                    );
                    try {
                        this.createStack(stackName,
                                "wordpress/wordpress-ha-aurora.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("ParentZoneStack").withParameterValue(zoneStackName),
                                new Parameter().withParameterKey("WebServerKeyName").withParameterValue(keyName),
                                new Parameter().withParameterKey("SubDomainNameWithDot").withParameterValue(subDomainName + "."),
                                new Parameter().withParameterKey("CloudFrontAcmCertificate").withParameterValue(Config.get(Config.Key.CLOUDFRONT_ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("ElbAcmCertificate").withParameterValue(Config.get(Config.Key.ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("BlogTitle").withParameterValue(blogTitle),
                                new Parameter().withParameterKey("BlogAdminUsername").withParameterValue("admin"),
                                new Parameter().withParameterKey("BlogAdminPassword").withParameterValue(blogPassword),
                                new Parameter().withParameterKey("BlogAdminEMail").withParameterValue("no-reply@widdix.de"),
                                new Parameter().withParameterKey("EFSBackupRetentionPeriod").withParameterValue("0")
                        );
                        final String url = "https://" + subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                        final Callable<String> callable = () -> {
                            final HttpResponse response = WS.url(url).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 200) {
                                throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                            }
                            return WS.getResponseAsString(response);
                        };
                        final String response = this.retry(callable);
                        // check if blog title appears in HTML
                        Assert.assertTrue("http response body contains \"" + blogTitle + "\"", response.contains(blogTitle));
                    } finally {
                        this.deleteStackAndRetryOnFailure(stackName);
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
    public void testAuroraWithEFSProvisionedThroughput() {
        final String zoneStackName = "zone-" + this.random8String();
        final String vpcStackName = "vpc-3azs-" + this.random8String();
        final String stackName = "wordpress-ha-aurora-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        final String subDomainName = stackName;
        final String blogTitle = "Stay-AWSome";
        final String blogPassword = this.random8String();
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
                            "vpc/vpc-3azs.yaml",
                            new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                    );
                    try {
                        this.createStack(stackName,
                                "wordpress/wordpress-ha-aurora.yaml",
                                new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                                new Parameter().withParameterKey("ParentZoneStack").withParameterValue(zoneStackName),
                                new Parameter().withParameterKey("WebServerKeyName").withParameterValue(keyName),
                                new Parameter().withParameterKey("SubDomainNameWithDot").withParameterValue(subDomainName + "."),
                                new Parameter().withParameterKey("CloudFrontAcmCertificate").withParameterValue(Config.get(Config.Key.CLOUDFRONT_ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("ElbAcmCertificate").withParameterValue(Config.get(Config.Key.ACM_CERTIFICATE_ARN)),
                                new Parameter().withParameterKey("BlogTitle").withParameterValue(blogTitle),
                                new Parameter().withParameterKey("BlogAdminUsername").withParameterValue("admin"),
                                new Parameter().withParameterKey("BlogAdminPassword").withParameterValue(blogPassword),
                                new Parameter().withParameterKey("BlogAdminEMail").withParameterValue("no-reply@widdix.de"),
                                new Parameter().withParameterKey("EFSProvisionedThroughputInMibps").withParameterValue("1"),
                                new Parameter().withParameterKey("EFSBackupRetentionPeriod").withParameterValue("0")
                        );
                        final String url = "https://" + subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                        final Callable<String> callable = () -> {
                            final HttpResponse response = WS.url(url).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 200) {
                                throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                            }
                            return WS.getResponseAsString(response);
                        };
                        final String response = this.retry(callable);
                        // check if blog title appears in HTML
                        Assert.assertTrue("http response body contains \"" + blogTitle + "\"", response.contains(blogTitle));
                    } finally {
                        this.deleteStackAndRetryOnFailure(stackName);
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

}
