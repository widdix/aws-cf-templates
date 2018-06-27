package de.widdix.awscftemplates.staticwebsite;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.taimos.httputils.WS;
import de.widdix.awscftemplates.ACloudFormationTest;
import de.widdix.awscftemplates.Config;
import org.apache.http.HttpResponse;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestStaticWebsite extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackNameLambdaEdge = "lambdaedge-index-document-" + this.random8String();
        final String zoneStackName = "zone-" + this.random8String();
        final String stackName = "static-website-" + this.random8String();
        final String subDomainName = stackName;
        final String redirectSubDomainName = "www-" + stackName;
        final String domainName = subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
        final String redirectDomainName = redirectSubDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
        try {
            this.createStack(zoneStackName,
                    "vpc/zone-legacy.yaml",
                    new Parameter().withParameterKey("HostedZoneName").withParameterValue(Config.get(Config.Key.DOMAIN_SUFFIX)),
                    new Parameter().withParameterKey("HostedZoneId").withParameterValue(Config.get(Config.Key.HOSTED_ZONE_ID))
            );
            try {
                this.createStack(stackNameLambdaEdge,
                        "static-website/lambdaedge-index-document.yaml",
                        new Parameter().withParameterKey("DomainName").withParameterValue(domainName),
                        new Parameter().withParameterKey("RedirectDomainName").withParameterValue(redirectDomainName)
                );
                final String lambdaVersionArn = this.getStackOutputValue(stackNameLambdaEdge, "LambdaVersionArn");
                try {
                    this.createStack(stackName,
                            "static-website/static-website.yaml",
                            new Parameter().withParameterKey("ParentZoneStack").withParameterValue(zoneStackName),
                            new Parameter().withParameterKey("SubDomainNameWithDot").withParameterValue(subDomainName + "."),
                            new Parameter().withParameterKey("LambdaEdgeSubdirectoriesVersionArn").withParameterValue(lambdaVersionArn),
                            new Parameter().withParameterKey("EnableRedirectSubDomainName").withParameterValue("true"),
                            new Parameter().withParameterKey("RedirectSubDomainNameWithDot").withParameterValue(redirectSubDomainName + "."),
                            new Parameter().withParameterKey("CertificateType").withParameterValue("AcmCertificateArn"),
                            new Parameter().withParameterKey("ExistingCertificate").withParameterValue(Config.get(Config.Key.CLOUDFRONT_ACM_CERTIFICATE_ARN))
                    );
                    final String url1 = "https://" + domainName + "/";
                    final String url2 = "https://" + domainName + "/folder/";
                    final String url3 = "https://" + redirectDomainName + "/";
                    try {
                        this.createObject(domainName, "index.html", "hello");
                        this.createObject(domainName, "folder/index.html", "hello");
                        final Callable<HttpResponse> callable1 = () -> {
                            final HttpResponse response = WS.url(url1).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 200) {
                                throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                            }
                            return response;
                        };
                        final Callable<HttpResponse> callable2 = () -> {
                            final HttpResponse response = WS.url(url2).timeout(10000).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 200) {
                                throw new RuntimeException("200 expected, but saw " + WS.getStatus(response));
                            }
                            return response;
                        };
                        final Callable<HttpResponse> callable3 = () -> {
                            final HttpResponse response = WS.url(url3).timeout(10000).followRedirect(false).get();
                            // check HTTP response code
                            if (WS.getStatus(response) != 301) {
                                throw new RuntimeException("301 expected, but saw " + WS.getStatus(response));
                            }
                            return response;
                        };
                        this.retry(callable1);
                        this.retry(callable2);
                        this.retry(callable3);
                    } finally {
                        this.deleteObject(domainName, "folder/index.html");
                        this.deleteObject(domainName, "index.html");
                    }
                } finally {
                    this.deleteStackAndRetryOnFailure(stackName);
                }
            } finally {
                // this stack is not deleted because Lambda@Edge functions take up to 3 hours before they can be deleted
                // the cleanup Lambda takes care of those stacks
                // this.deleteStackAndRetryOnFailure(stackNameLambdaEdge);
            }
        } finally {
            this.deleteStack(zoneStackName);
        }
    }
}
