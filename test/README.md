# Free Templates for AWS CloudFormation

Tests for our free Templates for AWS CloudFormation. The goal of this tests is to ensure that our templates are always working. The test are implemented in Java 8 and run in JUnit 4.

If you run this tests, many AWS CloudFormation tests are created and **charges will apply**!

[widdix GmbH](https://widdix.net) sponsors the test runs on every push and once per week to ensure that everything is working as expected.

## Prerequisites

To run this tests you need:
* A resolvable Route 53 hosted zone where record sets can be added automatically (**NOT** your production environment!)
* A domain suffix that cen be used for subdomains (e.g. `awstest.mydomain.com`)
* A wildcard ACM certificate in `us-east-1` that matches with the domain suffix from above (e.g. `*.awstest.mydomain.com`)
* A wildcard ACM certificate in the region you want to run this tests in like above

## Supported env variables

* `HOSTED_ZONE_ID` **required** A hosted zone id of your Route 53 hosted zone where the tests can create record sets
* `DOMAIN_SUFFIX` **required** A domain suffix that is part of your hosted zone
* `CLOUDFRONT_ACM_CERTIFICATE_ARN` **required** A wildcard ACM certificate in `us-east-1`
* `ACM_CERTIFICATE_ARN` **required** A wildcard ACM certificate in the region where the tests run
* `IAM_ROLE_ARN` if the tests should assume an IAM role before they run supply the ARN of the IAM role
* `TEMPLATE_DIR` Load templates from local disk (instead of S3 bucket `widdix-aws-cf-templates`). Must end with an `/`. See `BUCKET_NAME` as well.
* `BUCKET_NAME` Some templates are to big to be passed as a string from local disk, therefore you need to supply the name of the bucket that is used to upload templates.
* `BUCKET_REGION` **required if BUCKET_NAME is set** Region of the bucket
* `DELETION_POLICY` (default `delete`, allowed values [`delete`, `retain`]) should resources be deleted?
* `FAILURE_POLICY` (default `rollback`, allowed values [`rollback`, `retain`]) what happens if a stack fails?

## Usage

### AWS credentials

The AWS credentials are passed in as defined by the AWS SDK for Java: http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html

One addition: you can supply the env variable `IAM_ROLE_ARN` which let's the tests assume a role with the default credentials before running the tests.

### Region selection

The region selection works like defined by the AWS SDK for Java: http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/java-dg-region-selection.html

### Run all tests

```
AWS_REGION="us-east-1" HOSTED_ZONE_ID="..." DOMAIN_SUFFIX="..." CLOUDFRONT_ACM_CERTIFICATE_ARN="..." ACM_CERTIFICATE_ARN="..." mvn test
```

### Run a single test suite

to run the `TestJenkins` tests:

```
AWS_REGION="us-east-1" HOSTED_ZONE_ID="..." DOMAIN_SUFFIX="..." CLOUDFRONT_ACM_CERTIFICATE_ARN="..." ACM_CERTIFICATE_ARN="..." mvn -Dtest=TestJenkins test
```

### Run a single test

to run the `TestJenkins.testHA` test:

```
AWS_REGION="us-east-1" HOSTED_ZONE_ID="..." DOMAIN_SUFFIX="..." CLOUDFRONT_ACM_CERTIFICATE_ARN="..." ACM_CERTIFICATE_ARN="..." mvn -Dtest=TestJenkins#testHA test
```

### Load templates from local file system

```
AWS_REGION="us-east-1" HOSTED_ZONE_ID="..." DOMAIN_SUFFIX="..." CLOUDFRONT_ACM_CERTIFICATE_ARN="..." ACM_CERTIFICATE_ARN="..." BUCKET_REGION="..." BUCKET_NAME="..." TEMPLATE_DIR="/path/to/widdix-aws-cf-templates/" mvn test
```

### Assume role

This is useful if you run on a integration server like Jenkins and want to assume a different IAM role for this tests.

```
IAM_ROLE_ARN="arn:aws:iam::ACCOUNT_ID:role/ROLE_NAME" mvn test
```
