# Jenkins Templates for AWS CloudFormation

## Jenkins 2.0: highly available master/manager
This template describes a Jenkins master/manager in a highly available manner. If the instance fails it will be replaced automatically. All data stored on EFS where it is replicated across AZs and the file system can grow without a limit. The Jenkins master/manager sits behind a load balancer to provide a fixed endpoint.

![Architecture](./jenkins2-ja.png?raw=true "Architecture")

### Installation Guide
1. This templates depends on one of our [VPC templates](../vpc/). Please create a VPC stack first: <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-2azs.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=ec2-auto-recovery&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/jenkins/jenkins2-ha.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Grab the `MasterEndpoint` of the Jenkins master/manager from the **Outputs** tab of your stack.

## Support
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [team@widdix.de](mailto:team@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
