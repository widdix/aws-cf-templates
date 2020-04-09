<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v10 to v11

## static-website/lambdaedge-index-document

* The output `LambdaVersionArn` was renamed to `ViewerRequestLambdaEdgeFunctionVersionARN`.

## static-website/static-website

* Parameter `LambdaEdgeSubdirectoriesVersionArn` was removed. If you use the `static-website/lambdaedge-index-document` template, pass the `ViewerRequestLambdaEdgeFunctionVersionARN` output to `static-website/static-website` via the new `ViewerRequestLambdaEdgeFunctionVersionARN` parameter.

## Deprecation warnings

none

## Deprecations

* `operations/backup-dynamodb-native`: Template ws removed (use state/dynamodb instead with AWS Backup enabled, deprecated since v9)
* `ecs/cluster`: The parameter `DesiredCapacity` was removed (not needed anymore, deprecated since v9)
* `enkins/jenkins2-ha-agents`: The parameter `AgentDesiredCapacity` was removed (not needed anymore, deprecated since v9)
* `state/rds-aurora-serverless`: The parameter `Engine` was removed (replaced by `EngineVersion`, deprecated since v9)
