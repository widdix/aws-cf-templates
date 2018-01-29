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
        final String zoneStackName = "zone-" + this.random8String();
        final String stackName = "static-website-" + this.random8String();
        final String subDomainName = this.generateDomain(stackName);
        final String redirectSubDomainName = this.generateDomain("www-" + stackName);
        try {
            this.createStack(zoneStackName,
                    "vpc/zone-legacy.yaml",
                    new Parameter().withParameterKey("HostedZoneName").withParameterValue(Config.get(Config.Key.DOMAIN_SUFFIX)),
                    new Parameter().withParameterKey("HostedZoneId").withParameterValue(Config.get(Config.Key.HOSTED_ZONE_ID))
            );
            try {
                this.createStack(stackName,
                        "static-website/static-website.yaml",
                        new Parameter().withParameterKey("SubDomainNameWithDot").withParameterValue(subDomainName + "."),
                        new Parameter().withParameterKey("EnableRedirectSubDomainName").withParameterValue("true"),
                        new Parameter().withParameterKey("RedirectSubDomainNameWithDot").withParameterValue(redirectSubDomainName + "."),
                        new Parameter().withParameterKey("CertificateType").withParameterValue("AcmCertificateArn"),
                        new Parameter().withParameterKey("ExistingCertificate").withParameterValue(Config.get(Config.Key.CLOUDFRONT_ACM_CERTIFICATE_ARN))
                );
                final String url1 = "https://" + subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                final Callable<HttpResponse> callable1 = () -> {
                    final HttpResponse response = WS.url(url1).timeout(10000).get();
                    // check HTTP response code
                    if (WS.getStatus(response) != 404) {
                        throw new RuntimeException("404 expected, but saw " + WS.getStatus(response));
                    }
                    return response;
                };
                this.retry(callable1);
                final String url2 = "https://" + subDomainName + "." + Config.get(Config.Key.DOMAIN_SUFFIX);
                final Callable<HttpResponse> callable2 = () -> {
                    final HttpResponse response = WS.url(url2).timeout(10000).get();
                    // check HTTP response code
                    if (WS.getStatus(response) != 404) {
                        throw new RuntimeException("404 expected, but saw " + WS.getStatus(response));
                    }
                    return response;
                };
                this.retry(callable2);
            } finally {
                this.deleteStackAndRetryOnFailure(stackName);
            }
        } finally {
            this.deleteStack(zoneStackName);
        }
    }

}
