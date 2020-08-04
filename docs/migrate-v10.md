<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v9 to v10

## *

If you have `SystemsManagerAccess` set to `true`, we previously attached the managed policy `AmazonEC2RoleforSSM` but now only attach the following IAM permissions:

* `ssmmessages:*`
* `ssm:UpdateInstanceInformation`
* `ec2messages:*`

This reducdes the permissions but is sufficient to make SSM Session Manager and Run Commands work.

To restore the previous permissions (which are not following the least privilege principle), set the new parameter `ManagedPolicyArns` to `arn:aws:iam::aws:policy/service-role/AmazonEC2RoleforSSM`.

## fargate/service-*

* Rename parameter from `AmbassadorImage` to `ProxyImage`.
* Rename parameter from `AmbassadorCommand` to `ProxyCommand`.
* Rename parameter from `AmbassadorPort` to `ProxyPort`.
* Rename parameter from `AmbassadorEnvironment1Key` to `ProxyEnvironment1Key`.
* Rename parameter from `AmbassadorEnvironment1Value` to `ProxyEnvironment1Value`.
* Rename parameter from `AmbassadorEnvironment2Key` to `ProxyEnvironment2Key`.
* Rename parameter from `AmbassadorEnvironment2Value` to `ProxyEnvironment2Value`.
* Rename parameter from `AmbassadorEnvironment3Key` to `ProxyEnvironment3Key`.
* Rename parameter from `AmbassadorEnvironment3Value` to `ProxyEnvironment3Value`.

## Deprecation warnings

* `ecs/cluster`: The parameter `DesiredCapacity` will be removed in v11 (not needed anymore)
* `jenkins/jenkins2-ha-agents`: The parameter `AgentDesiredCapacity` will be removed in v11 (not needed anymore)
* `state/rds-aurora-serverless`: The parameter `Engine` will be removed in v11 (replaced by `EngineVersion`)
