# aws-cf-templates

A VPC is a virtual network inside AWS where you can isolate your setup using private IP addresses. A VPC consists of several subnets. Each subnet is bound to an Availability Zone. A **public** subnet has a direct route to the Internet. As long as your EC2 instances have an public IP they can communicate (in and out) with the Internet. A **private** subnet does not have a route to the Internet. Instances in private subnets can not be accessed from the public Internet. If you want to access the Internet from a private subnet you need to create a NAT instance.

## VPC with private and public subnets in two Availability Zones

Use the `vpc-2azs.json` template to create a VPC with two private and two public subnets.

### Components

#### AWS services

* VPC: virtual network that you define

### Installation Guide

1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the JSON-file `vpc-2azs.json` from this repository.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## VPC with private and public subnets in three Availability Zones

Use the `vpc-3azs.json` template to create a VPC with three private and two public subnets.

### Components

#### AWS services

* VPC: virtual network that you define

### Installation Guide

1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the JSON-file `vpc-3azs.json` from this repository.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Nat instance

Use the `vpc-nat-instance.json` template to create a **highly available** NAT instance that forwards HTTP, HTTPS and NTP traffic from private subnets to the Internet.

### Components

#### AWS services

* EC2: virtual machine used as the NAT instance
* Auto Scaling: manages the fleet of virtual machines

### Installation Guide

1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the JSON-file `vpc-nat-instance.json` from this repository.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

### Limitations

## Support needed?

Do you need help? Mail to [team@widdix.de](mailto:team@widdix.de).
