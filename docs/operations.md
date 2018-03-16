<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

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
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=operations-alert&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/alert.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

# Backup DynamoDB (Data Pipeline & EMR)
This template describes a Data Pipeline to backup a single DynamoDB table. The Data Pipeline will spin up a EMR cluster to do the backup.

> Deprecated, use `operations/backup-dynamodb-native.yaml` instead!

## Installation Guide
1. This templates depends on our [`vpc-*azs.yaml`](../vpc/) template. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. This templates depends on our `alert.yaml` template. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=operations-alert&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/alert.yaml)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=operations-backup-dynamodb&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/backup-dynamodb.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `operations/alert.yaml` (**required**)

## Limitations
* The EMR cluster will only run in a single subnet (`SubnetAPublic`)

# Backup DynamoDB (native)
This template describes a Lambda function to backup a single DynamoDB table daily.

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=operations-backup-dynamodb-native&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/operations/backup-dynamodb-native.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Dependencies
* `operations/alert.yaml` (recommended)
