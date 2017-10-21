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
        final String stackName = "static-website-" + this.random8String();
        final String domainName = this.generateDomain(stackName);
        final String redirectDomainName = this.generateDomain("www-" + stackName);
        try {
            this.createStack(stackName,
                    "static-website/static-website.yaml",
                    new Parameter().withParameterKey("DomainName").withParameterValue(domainName),
                    new Parameter().withParameterKey("RedirectDomainName").withParameterValue(redirectDomainName),
                    new Parameter().withParameterKey("CertificateType").withParameterValue("AcmCertificateArn"),
                    new Parameter().withParameterKey("ExistingCertificate").withParameterValue(Config.get(Config.Key.CLOUDFRONT_ACM_CERTIFICATE_ARN)),
                    new Parameter().withParameterKey("HostedZoneId").withParameterValue(Config.get(Config.Key.HOSTED_ZONE_ID))
            );
            final String url = "https://" + domainName;
            final Callable<HttpResponse> callable = () -> {
                final HttpResponse response = WS.url(url).timeout(10000).get();
                // check HTTP response code
                if (WS.getStatus(response) != 404) {
                    throw new RuntimeException("404 expected, but saw " + WS.getStatus(response));
                }
                return response;
            };
            this.retry(callable);
        } finally {
            this.deleteStackAndRetryOnFailure(stackName);
        }
    }

}
