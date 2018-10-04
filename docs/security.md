<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: Manage Free Templates for AWS CloudFormation with the [widdix CLI](./cli/)

# S3 VirusScan
This template creates a Antivirus cluster for S3 buckets. You can connect as many buckets as you like by using [S3 Event Notifications](http://docs.aws.amazon.com/AmazonS3/latest/dev/NotificationHowTo.html). The template has it's own repository: [aws-s3-virusscan](https://github.com/widdix/aws-s3-virusscan)

## Features

* Uses ClamAV to scan newly added files on S3 buckets
* Updates ClamAV database every 3 hours automatically
* Scales EC2 instance workers to distribute workload
* Publishes a message to SNS in case of a finding
* Can optionally delete compromised files automatically
* Logs to CloudWatch Logs

## Installation Guide
Visit the template's repository for installation instructions: [aws-s3-virusscan](https://github.com/widdix/aws-s3-virusscan)

# Account Password Policy
This template creates an account password policy for your IAM users. You can:

* set IAM user passwords to be valid for only the specified number of days.
* prevent IAM users from reusing a specified number of previous passwords.
* specify the minimum number of characters allowed in an IAM user password.
* require that IAM user passwords contain at least one lowercase character from the ISO basic Latin alphabet (a to z).
* require that IAM user passwords contain at least one uppercase character from the ISO basic Latin alphabet (A to Z).
* require that IAM user passwords contain at least one numeric character (0 to 9).
* require that IAM user passwords contain at least one nonalphanumeric character.
* permit all IAM users in your account to use the IAM console to change their own passwords.
* prevent IAM users from choosing a new password after their current password has expired.

Or just use the suggested defaults.

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-account-password-policy&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/security/account-password-policy.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

# Authentication Proxy using your GitHub Organization
This template describes a **highly available** authentication proxy that forwards request to a upstream http(s) endpoint if the user is authenticated against your GitHub Organization.

![Architecture](./img/security-auth-proxy-ha-github-orga.png)

## Installation Guide
1. This templates depends on one of our [`vpc-*azs.yaml`](./vpc/) templates. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-auth-proxy-ha-github-orga&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/security/auth-proxy-ha-github-orga.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `vpc/vpc-*-bastion.yaml` (recommended)
* `operations/alert.yaml` (recommended)

## Limitations
* By default, only one EC2 instance is managed by the ASG. In case of an outage the instance will be replaced within 5 minutes. You can change this in the ASG configuration!

# CloudTrail across all regions
This template enables CloudTrail to records AWS API calls across all regions in your AWS account. API calls are archived in S3 and also pushed CloudWatch Logs. If new API calls are available in S3 a SNS topic is notified.

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-cloudtrail&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/security/cloudtrail.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

If you want to use an external S3 bucket, the bucket needs to have the following S3 bucket policy:

```
{
  "Version": "2012-10-17",
  "Statement": [{
    "Sid": "AWSCloudTrailAclCheck",
    "Effect": "Allow",
    "Principal": {
      "Service": "cloudtrail.amazonaws.com"
    },
    "Action": "s3:GetBucketAcl",
    "Resource": "arn:aws:s3:::$ExternalTrailBucket"
  }, {
    "Sid": "AWSCloudTrailWrite",
    "Effect": "Allow",
    "Principal": {
      "Service": "cloudtrail.amazonaws.com"
    },
    "Action": "s3:PutObject",
    "Resource": [
      "arn:aws:s3:::$ExternalTrailBucket/AWSLogs/$AccountId[0]/*",
      "arn:aws:s3:::$ExternalTrailBucket/AWSLogs/$AccountId[1]/*",
      "arn:aws:s3:::$ExternalTrailBucket/AWSLogs/$AccountId[2]/*"
    ],
    "Condition": {
      "StringEquals": {
        "s3:x-amz-acl": "bucket-owner-full-control"
      }
    }
  }]
}
```

Replace `$ExternalTrailBucket` with the name of your bucket, and add a row for every account you want to write from `$AccountId[*]`.

# AWS Config setup
This template enables AWS Config to deliver a AWS resource inventory to S3. Allowing you to keep track of infrastructure changes for compliance and debugging of your cloud infrastructure. 

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-config&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/security/config.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

If you want to use an external S3 bucket, the bucket needs to have the following S3 bucket policy:

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AWSConfigBucketPermissionsCheck",
      "Effect": "Allow",
      "Principal": {
        "Service": [
         "config.amazonaws.com"
        ]
      },
      "Action": "s3:GetBucketAcl",
      "Resource": "arn:aws:s3:::$ExternalConfigBucket"
    },
    {
      "Sid": " AWSConfigBucketDelivery",
      "Effect": "Allow",
      "Principal": {
        "Service": [
         "config.amazonaws.com"
        ]
      },
      "Action": "s3:PutObject",
      "Resource": [
        "arn:aws:s3:::$ExternalConfigBucket/AWSLogs/$AccountId[0]/Config/*",
        "arn:aws:s3:::$ExternalConfigBucket/AWSLogs/$AccountId[0]/Config/*",
        "arn:aws:s3:::$ExternalConfigBucket/AWSLogs/$AccountId[2]/Config/*"
      ],
      "Condition": {
        "StringEquals": {
          "s3:x-amz-acl": "bucket-owner-full-control" 
        }
      }
    }
  ]
}
```

Replace `$ExternalTrailBucket` with the name of your bucket, and add a row for every account you want to write from `$AccountId[*]`.
