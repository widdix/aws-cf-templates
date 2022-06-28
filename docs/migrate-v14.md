<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v13 to v14

## Version defaults removed

We removed default values for parameters selecting a version. AWS constantly deprecates versions, and therefore defaults will become unavailable over time.

### ec2/al2-mutable-private

Parameter `AmazonLinux2Version` default value was removed. Use `20191217.0` if you haven't specified the parameter value before.

### ec2/al2-mutable-public

Parameter `AmazonLinux2Version` default value was removed. Use `20191217.0` if you haven't specified the parameter value before.

### state/documentdb

Parameter `EngineVersion` default value was removed. Use `3.6.0` if you haven't specified the parameter value before.

### state/elasticache-memcached

Parameter `EngineVersion` default value was removed. Use `1.4.34` if you haven't specified the parameter value before.

### state/elasticache-redis

Parameter `EngineVersion` default value was removed. Use `5.0.0` if you haven't specified the parameter value before.

### state/elasticsearch

Parameter `ElasticsearchVersion` default value was removed. Use `5.5` if you haven't specified the parameter value before.

### state/rds-aurora-serverless-postgres

Parameter `EngineVersion` default value was removed. Use `10.7` if you haven't specified the parameter value before.

### state/rds-aurora-serverless

Parameter `EngineVersion` default value was removed. Use `5.6.10a` if you haven't specified the parameter value before.

### state/rds-aurora

Parameter `Engine` default value was removed. Use `aurora` if you haven't specified the parameter value before.

### state/rds-mysql

Parameter `EngineVersion` default value was removed. Use `5.7.21` if you haven't specified the parameter value before.

### state/rds-postgres

Parameter `EngineVersion` default value was removed. The previous default of `9.6.24` is no longer supported. Use `10.17` if you haven't specified the parameter value before.

## wordpress/*

We updated WordPress to version 6.0.

## Deprecation warnings

* `security/kms-key`: Value `dnssec-route53` for parameter `Service` will be removed in v15. Use `ROUTE53_DNSSEC` instead.

## Deprecations

None.
