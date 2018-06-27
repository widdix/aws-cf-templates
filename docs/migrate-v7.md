<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

# Migrate from v6 to v7

## static-website/static-website.yaml

You have to create a second stack based on `static-website/lambdaedge-index-document.yaml` for each stack based on `static-website.yaml`. [Learn more](../static-website/)

If you are using the `RedirectDomainName` parameter in `static-website.yaml`, update the stack with the new template version and remove the `RedirectDomainName` parameter. After the stack is updated (usually takes 15-30 mins because of CloudFront!), run a second update where you re-add the `RedirectDomainName` parameter.
