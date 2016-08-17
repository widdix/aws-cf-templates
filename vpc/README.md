# VPC Templates for AWS CloudFormation

A VPC is a virtual network inside AWS where you can isolate your setup using private IP addresses. A VPC consists of several subnets. Each subnet is bound to an Availability Zone. A **public** subnet has a direct route to the Internet. As long as your EC2 instances have an public IP they can communicate (in and out) with the Internet. A **private** subnet does not have a route to the Internet. Instances in private subnets can not be accessed from the public Internet. If you want to access the Internet from a private subnet you need to create a NAT instance.

## VPC with private and public subnets in two Availability Zones

This template describes a VPC with two private and two public subnets.

![Architecture](./vpc-2azs.png?raw=true "Architecture")

### Installation Guide

1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-2azs.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## VPC with private and public subnets in three Availability Zones

This template describes a VPC with three private and three public subnets.

![Architecture](./vpc-3azs.png?raw=true "Architecture")

### Installation Guide

1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-3azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-3azs.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## VPC with private and public subnets in four Availability Zones

This template describes a VPC with four private and four public subnets.

![Architecture](./vpc-4azs.png?raw=true "Architecture")

### Installation Guide

1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-4azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-4azs.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## NAT Gateway

This template describes a NAT Gateway that forwards HTTP, HTTPS and NTP traffic from private subnets to the Internet.

![Architecture](./vpc-nat-gateway.png?raw=true "Architecture")

### Installation Guide

1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-nat-gateway&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-nat-gateway.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## NAT instance

This template describes a **highly available** Network Address Translation (NAT) instance that forwards HTTP, HTTPS and NTP traffic from private subnets to the Internet.

![Architecture](./vpc-nat-instance.png?raw=true "Architecture")
### Installation Guide

1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-nat-instance&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-nat-instance.json"><img src="../cloudformation-launch-stack.png?raw=true" alt="Launch Stack"></a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Support
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [team@widdix.de](mailto:team@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
