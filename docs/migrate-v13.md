<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v12 to v13

## *

IMDSv2 is now enforced by default except `ec2/*` templates. If you have done any customization, follow the official [EC2 Migration Guide](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/configuring-instance-metadata-service.html#instance-metadata-transition-to-version-2).

## fargate/*

Update Platform version to 1.4.0. Check out the official [ECS Migration guide](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/platform_versions.html#platform-version-migration).

## Deprecation warnings

* `static-website/static-website`: Parameter `EnableRedirectSubDomainName` will be removed in v14. Use `static-website/lambdaedge-index-document` instead. (deprecated since v12)
* `static-website/static-website`: Parameter `RedirectSubDomainNameWithDot` will be removed in v14. Use `static-website/lambdaedge-index-document` instead. (deprecated since v12)

## Deprecations

* `state/dynamodb`: Parameter `Encryption` was removed. Use parameter `ParentKmsKeyStack` instead. (deprecated since v11)
* `ec2/ec2-auto-recovery`: Template removed. Use `ec2/al2-mutable-public` or `ec2/al2-mutable-private` instead. (deprecated since v11)
