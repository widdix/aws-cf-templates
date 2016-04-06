# aws-cf-templates

## CloudTrail across all regions

Use the `cloudtrail.json` template to setup CloudTrail across all regions.

### Components

#### AWS services

* CloudTrail: records AWS API calls
* S3: object storage
* CloudWatch Logs: search and monitor your logs

### Installation Guide

1. Download the template [cloudtrail.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/security/cloudtrail.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `cloudtrail.json`.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Account Password Policy

Use the `account-password-policy.json` template to create a account password policy for your IAM users.

### Components

#### AWS services

* IAM: Identity & Access Management
* Lambda: Used to implement the custom resource in the CloudFormation template

### Installation Guide

1. Download the template [account-password-policy.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/security/account-password-policy.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `account-password-policy.json`.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Support needed?

Do you need help? Mail to [team@widdix.de](mailto:team@widdix.de).
