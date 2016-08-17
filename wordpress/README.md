# Wordpress Templates for AWS CloudFormation

## Wordpress: fault tolerant + scalable
This template describes a **fault tolerant** and **scalable** Wordpress environment.

![Architecture](./wordpress-ha.png?raw=true "Architecture")

### Installation Guide
1. This templates depends on one of our [VPC templates](/vpc-templates-for-aws-cloudformation/). Please create a VPC stack first: <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-2azs.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=wordpress-ha&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/wordpress/wordpress-ha.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Grab the URL of the Wordpress environment from the **Outputs** tab of your stack.

### Limitations
Installing and updating core, plugins and themes is disabled. You need to edit the `/root/config.sh` to change core, plugins and themes during bootstrapping of EC2 instances.

## Support
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [team@widdix.de](mailto:team@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
