<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: Manage Free Templates for AWS CloudFormation with the [widdix CLI](./cli/)

# Migrate from v8 to v9

> WARNING: Data loss if you don't follow the guidlines!

## state/elasticsearch, state/rds-aurora, state/rds-mysql, state/rds-postgres, state/s3

> WARNING: Ensure that your current v8 version is >= 8.1.1. If you run on version 8.0.0-8.1.0 update to the latest v8 before! Otherwise you will lose data!

1. If `Encryption` parameter is `true`, you have to follow this steps to avoid data loss:
    1. For each `state/elasticsearch`, `state/rds-aurora`, `state/rds-mysql`, `state/rds-postgres`, `state/s3` stack:
        1. Fetch the KMS key id from the existing stack
        2. Create an additional stack in the same region based on the `security/kms-key-legacy.yaml` template.
        3. Use the existing KMS key id as the `KeyId` parameter of the new stack
        4. Update the existing stack to v9 and set the new `ParentKmsKeyStack` parameter to the stack name of the stack you created in the step before.

## Deprecation warnings

none
