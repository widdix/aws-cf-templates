<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v11 to v12

## *

IPv6 support for subnets, public load balancers, and CloudFront. EC2 instances still use IPv4 only by default! Nothing needs to be done!

## vpc/vpc-[2-4]azs-legacy

* Parameter `CidrBlockIPv6` was added: Add IPv6 support to your legacy VPC.
* Parameter `InternetGateway` was added: Pass the IWG id of your legacy VPC.

## Deprecation warnings

* `ec2/ec2-auto-recovery`: Template will be removed in v13. Use `ec2/al2-mutable-public` or `ec2/al2-mutable-private` instead. (deprecated since v11)
* `state/dynamodb`: Parameter `Encryption` will be removed in v13. Use parameter `ParentKmsKeyStack` instead. (deprecated since v11)

## Deprecations

none
