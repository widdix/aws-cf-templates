<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Alert topic
This template describes a SNS topic that can be used by many other templates to receive alerts. You can add one or multiple subscribers to this topic and they will all receive the same alerts. Supported transports are:
* Email
* HTTP endpoint
* HTTPS endpoint (can be used by [marbot](https://marbot.io/?utm_source=templates&utm_medium=doc&utm_campaign=operations))

![Architecture](./img/operations-alert.png)

## marbot

![marbot](https://marbot.io/assets/marbot.png)

Hi, my name is marbot.

I'm a Slack bot supporting your DevOps team to detect and solve incidents on AWS.

I help you to set up AWS monitoring. There are countless possibilities on AWS. Overlooking the important settings is easy. I connect you with all relevant AWS sources. You never miss an incident again.

Don’t get distracted from your deep work, when not absolutely necessary. I do send alerts to a single team member. Of course, I escalate unnoticed alerts to another team member or the whole crew if necessary.

Instead of cluttering up your inbox with emails I do send alerts via Slack. Just re-use your modern team communication solution. Invite me to multiple Slack channels to separate alerts. You can also talk to me.

I add links to AWS Management Console that are relevant to an incident. Contextual links save you time and reduce human error in stressful situations.

[Try marbot for free now!](https://marbot.io/?utm_source=templates&utm_medium=doc&utm_campaign=operations)

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/create/review?templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/alert.yaml&stackName=alert)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

# Access Logs Anonymizer

IPv4 addresses are anonymized to `XXX.YYY.ZZZ.0` and IPv6 addresses to `XXXX:YYYY::`.

Access logs are stored in S3 buckets (created via [state/s3](./state/#s3)). The following order of creation is recommended:

1. Create [S3 Bucket](./state/#s3) stack.
2. Create Access Logs Anonymizer stack.
3. Update S3 Bucket stack and set the parameter **LambdaFunctionArn** to the **FunctionARN** output of the Access Logs Anonymizer stack.

## CloudFront
This template describes a Lambda function that can be used to anonymize IP addresses in CloudFront access logs. 

## Installation Guide
1. This template depends on our `state/s3.yaml` template. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/create/review?templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/state/s3.yaml&stackName=access-logs&param_Access=CloudFrontAccessLogWrite)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/create/review?templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/cloudfront-access-logs-anonymizer.yaml&stackName=access-logs-anonymizer&param_ParentS3Stack=access-logs)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Update S3 Bucket stack and set the parameter **LambdaFunctionArn** to the **FunctionARN** output of the Access Logs Anonymizer stack.

### Dependencies
* `state/s3.yaml` (**required**)
* `operations/alert.yaml` (recommended)

## ALB
This template describes a Lambda function that can be used to anonymize IP addresses in ALB access logs. 

## Installation Guide
1. This template depends on our `state/s3.yaml` template. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/create/review?templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/state/s3.yaml&stackName=access-logs&param_Access=ElbAccessLogWrite)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/create/review?templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/alb-access-logs-anonymizer.yaml&stackName=access-logs-anonymizer&param_ParentS3Stack=access-logs)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Update S3 Bucket stack and set the parameter **LambdaFunctionArn** to the **FunctionARN** output of the Access Logs Anonymizer stack.

### Dependencies
* `state/s3.yaml` (**required**)
* `operations/alert.yaml` (recommended)

# Terraform State

Creates S3 bucket and DynamoDB table used to manage remote Terraform state.

## Installation Guide
1. This template depends on our `security/kms.yaml` template. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/create/review?templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/security/kms-key.yaml&stackName=kms-key&param_Service=s3)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/create/review?templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/terraform-state.yaml&stackName=terraform-state&param_ParentKMSKeyStack=kms-key)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

> Be aware that the template creates a bucket policy using a `Deny` statement with a `NotPrincipal` element when defining the `TerraformStateUserARNs` and `TerraformStateAdminARNs` parameters. Therefore, both parameters should include the following inforamtion: account ARN (e.g., `arn:aws:iam::111111111111:root`), IAM user (e.g., `arn:aws:iam::111111111111:user/tfuser`), IAM role (e.g., `arn:aws:iam::111111111111:role/tfadmin`) and assumed-role user (e.g., `arn:aws:sts::111111111111:assumed-role/tfadmin/session`). Check out [NotPrincipal with Deny](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_elements_notprincipal.html#specifying-notprincipal-allow) to learn more.

### Dependencies
* `security/kms-key.yaml` (**required**)
