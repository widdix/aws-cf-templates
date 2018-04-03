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
    }

}
