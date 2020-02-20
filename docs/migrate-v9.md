<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v8 to v9

> WARNING: Follow this guideline to avoid data loss!

## state/s3

> WARNING: Ensure that your current v8 version is >= 8.1.1. If you run on version 8.0.0-8.1.0 update to the latest v8 before! Otherwise, you will lose data!

> WARNING: Test the migration in a non-production setup (don't forget to validate your data) before you update production!

1. Update all existing `security/kms-key-legacy` stacks to v9.
2. For each `state/s3` stack with `Encryption` parameter set to `true`:
    1. Fetch the KMS key id from the existing stack (open the *Resources* tab of the stack. In the *Logical ID* column search for `Key`. In the same row, the column *Physical ID* shows the KMS key id)
    2. Create an additional stack in the same region based on the `security/kms-key-legacy.yaml` template (ensure you use v9!). Use the existing KMS key id as the `KeyId` parameter.
    3. Update the existing `state/s3` stack to v9 and set the new `ParentKmsKeyStack` parameter to the stack name of the stack you created in step 2.2.

## state/rds-mysql, state/rds-postgres

> WARNING: Ensure that your current v8 version is >= 8.1.1. If you run on version 8.0.0-8.1.0 update to the latest v8 before! Otherwise, you will lose data!

> WARNING: Test the migration in a non-production setup (don't forget to validate your data) before you update production!

1. Update all existing `security/kms-key-legacy` stacks to v9.
2.For each `state/rds-mysql` or `state/rds-postgres` stack with `Encryption` parameter set to `true`:
    1. Fetch the KMS key id from the existing stack (open the *Resources* tab of the stack. In the *Logical ID* column search for `Key`. In the same row, the column *Physical ID* shows the KMS key id)
    2. Stop all write requests to the cluster
    3. Take a [manual RDS snapshot](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_CreateSnapshot.html)
    4. Create an additional stack in the same region based on the `security/kms-key-legacy.yaml` template (ensure you use v9!). Use the existing KMS key id as the `KeyId` parameter.
    5. Update the existing `state/rds-mysql` or `state/rds-postgres` stack to v9 and
        a. Set the new `ParentKmsKeyStack` parameter to the stack name of the stack you created in step 2.4.
        b. Set the `DBSnapshotIdentifier` parameter to the ARN of the snapshot you created in step 2.3.

## state/rds-aurora

> WARNING: Ensure that your current v8 version is >= 8.1.1. If you run on version 8.0.0-8.1.0 update to the latest v8 before! Otherwise, you will lose data!

> WARNING: Test the migration in a non-production setup (don't forget to validate your data) before you update production!

1. Update all existing `security/kms-key-legacy` stacks to v9.
2.For each `state/rds-aurora` stack with `Encryption` parameter set to `true`:
    1. Fetch the KMS key id from the existing stack (open the *Resources* tab of the stack. In the *Logical ID* column search for `Key`. In the same row, the column *Physical ID* shows the KMS key id)
    2. Stop all write requests to the cluster
    3. Take a [manual RDS snapshot](hhttps://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/USER_CreateSnapshotCluster.html)
    4. Create an additional stack in the same region based on the `security/kms-key-legacy.yaml` template (ensure you use v9!). Use the existing KMS key id as the `KeyId` parameter.
    5. Update the existing `state/rds-aurora` stack to v9 and 
        a. Set the new `ParentKmsKeyStack` parameter to the stack name of the stack you created in step 2.4.
        b. Set the `DBSnapshotIdentifier` parameter to the ARN of the snapshot you created in step 2.3.

# state/elasticsearch

> WARNING: Ensure that your current v8 version is >= 8.1.1. If you run on version 8.0.0-8.1.0 update to the latest v8 before! Otherwise, you will lose data!

> WARNING: Test the migration in a non-production setup (don't forget to validate your data) before you update production!

1. Update all existing `security/kms-key-legacy` stacks to v9.
2. For each `state/elasticsearch` stack with `Encryption` parameter set to `true`:
    1. Fetch the KMS key id from the existing stack (open the *Resources* tab of the stack. In the *Logical ID* column search for `Key`. In the same row, the column *Physical ID* shows the KMS key id)
    2. Stop all write requests to the cluster
    3. Take a [manual snapshot](https://docs.aws.amazon.com/elasticsearch-service/latest/developerguide/es-managedomains-snapshots.html#es-managedomains-snapshot-create)
    4. Create an additional stack in the same region based on the `security/kms-key-legacy.yaml` template (ensure you use v9!). Use the existing KMS key id as the `KeyId` parameter.
    5. Update the existing `state/elasticsearch` stack to v9 and set the new `ParentKmsKeyStack` parameter to the stack name of the stack you created in step 2.4.
    6. [Restore](https://docs.aws.amazon.com/elasticsearch-service/latest/developerguide/es-managedomains-snapshots.html#es-managedomains-snapshot-restore) the snapshot created in step 2.3.

## Deprecation warnings

* `ecs/cluser`: Parameter `DesiredCapacity` will be removed in v11, use `MinSize` instead
* `jenkins/jenkins2-ha-agents`: Parameter `AgentDesiredCapacity` will be removed in v11, use `AgentMinSize` instead
