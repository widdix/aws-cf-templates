<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v6 to v7

## ec2/ec2-auto-recovery, security/auth-proxy-ha-github-orga, vpc/vpc-ssh-bastion

1. Amazon Linux is updated to Amazon Linux 2.
2. The parameter `SubDomainName` was renamed to `SubDomainNameWithDot` and now expects that your name end with a dot. E.g., if the value of `SubDomainName` is `ssh`, set `SubDomainNameWithDot` to `ssh.`.
3. `SystemsManagerAccess` will be enabled by default.

## ecs/service-cluster-alb, ecs/service-dedicated-alb, vpc/vpc-nat-instance, state/rds-postgres, state/elasticsearch, state/elasticache-memcached

1. The parameter `SubDomainName` was renamed to `SubDomainNameWithDot` and now expects that your name end with a dot. E.g., if the value of `SubDomainName` is `ssh`, set `SubDomainNameWithDot` to `ssh.`.

## state/rds-aurora

1. The parameter `SubDomainName` was renamed to `SubDomainNameWithDot` and now expects that your name end with a dot. E.g., if the value of `SubDomainName` is `ssh`, set `SubDomainNameWithDot` to `ssh.`.
2. The implicit name of the Aurora read endpoint is now explicit and can be configured with the `ReadSubDomainNameWithDot` parameter. E.g., if the value of `SubDomainName` is `aurora`, set `ReadSubDomainNameWithDot` to `read-aurora.` to get the same name as before.

## jenkins/jenkins-*

1. Amazon Linux is updated to Amazon Linux 2.
2. The parameter `SubDomainName` was renamed to `SubDomainNameWithDot` and now expects that your name end with a dot. E.g., if the value of `SubDomainName` is `ssh`, set `SubDomainNameWithDot` to `ssh.`.
3. `SystemsManagerAccess` will be enabled by default.
4. The parameter `JenkinsVersion` was removed to make updates possible.

## static-website/static-website

The new static website makes use of Lambda@Edge.

1. For each `static-website/static-website` stack, you have to create an additional stack in `us-east-1` based on the new `static-website/lambdaedge-index-document.yaml` template. [Learn more](./static-website/)
2. If you are using the `RedirectDomainName` parameter in `static-website/static-website`, update the stack with the new template version and remove the `RedirectDomainName` parameter value. After the stack is updated (usually takes 15-30 mins because of CloudFront!), continue with the next step.
3. The optional parameter `LambdaEdgeSubdirectoriesVersionArn` was added. The value should be the `LambdaVersionArn` output of the `static-website/lambdaedge-index-document` stack.
4. The `DefaultRootObject` parameter was added but is only used if `LambdaEdgeSubdirectoriesVersionArn` is not set. Usually you will set this to something like `index.html` which is the default as well.
5. The following parameters have been removed:
    1. `DomainName` is replaced by `SubDomainNameWithDot` and now expects that your name end with a dot. E.g., if the value of `SubDomainName` is `ssh`, set `SubDomainNameWithDot` to `ssh.`.
    2. `RedirectDomainName` is replaced by `EnableRedirectSubDomainName` and `RedirectSubDomainNameWithDot`. If you want to have a second domain to redirect to the primary domain, enable `EnableRedirectSubDomainName` and provide the `RedirectSubDomainNameWithDot` as well.
    3. `HostedZoneId` is replaced by `ParentZoneStack`

## vpc/vpc-*azs-legacy

1. The parameter `CidrBlock` was added and will replace `ClassB` in v8. E.g., if the value of `CidrBlock` is `100`, set `CidrBlock` to `10.100.0.0/16`.

## vpc/vpc-ssh-bastion

1. Before you can update stacks based on `vpc/vpc-ssh-bastion` you have to set all `ParentSSHBastionStack` parameters of other stacks to an empty value, update the bastion host stack, and then set the `ParentSSHBastionStack` parameter values back to the previous value.

## wordpress/wordpress-ha*

1. Amazon Linux is updated to Amazon Linux 2.
2. The parameter `DomainName` was renamed to `SubDomainNameWithDot` and `ParentZoneStack` is now required (was optional before). E.g., if the value of `DomainName` is `www.widdix.de`, set `SubDomainNameWithDot` to `www.` and the `ParentZoneStack` should have the `Name` parameter set to `widdix.de`.
3. The parameter `SubDomainName` is replaced by `SubDomainNameWithDot`. E.g., if the value of `SubDomainName` is `ssh`, set `SubDomainNameWithDot` to `ssh.`.
4. The parameter `DBMasterUserPassword` was added (was hard coded to `wordpress` before).
5. `SystemsManagerAccess` will be enabled by default.
6. The parameter `BlogVersion` was removed to make updates possible.

## Deprecation warnings

* vpc/vpc-*azs-legacy: Paramater `ClassB` and output `ClassB` will be removed in the next version (v8).

