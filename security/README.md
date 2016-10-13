# Security Templates for AWS CloudFormation

## Account Password Policy
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

### Installation Guide
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-account-password-policy&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/security/account-password-policy.json">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Authentication Proxy using your GitHub Organization
This template describes a **highly available** authentication proxy that forwards request to a upstream http(s) endpoint if the user is authenticated against your GitHub Organization.

### Installation Guide
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-auth-proxy-ha-github-orga&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/security/auth-proxy-ha-github-orga.json">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## CloudTrail across all regions
This template enables CloudTrail to records AWS API calls across all regions in your AWS account. API calls are archived in S3 and also pushed CloudWatch Logs. If new API calls are available in S3 a SNS topic is notified.

### Installation Guide
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-cloudtrail&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/security/cloudtrail.json">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## AWS Config setup
This template enables AWS Config to deliver a AWS resource inventory to S3. Allowing you to keep track of infrastructure changes for compliance and debugging of your cloud infrastructure. 

### Installation Guide
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=security-config&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/security/config.json">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
## Support needed?
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments.

## Support
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [team@widdix.de](mailto:team@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
