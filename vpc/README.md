# aws-cf-templates

A VPC is a virtual network inside AWS where you can isolate your setup using private IP addresses. A VPC consists of several subnets. Each subnet is bound to an Availability Zone. A **public** subnet has a direct route to the Internet. As long as your EC2 instances have an public IP they can communicate (in and out) with the Internet. A **private** subnet does not have a route to the Internet. Instances in private subnets can not be accessed from the public Internet. If you want to access the Internet from a private subnet you need to create a NAT instance.

## VPC with private and public subnets in two Availability Zones

Use the `vpc-2azs.json` template to create a VPC with two private and two public subnets.

### Architecture

![Architecture](./vpc-2azs.png?raw=true "Architecture")

### Components

#### AWS services

* VPC: virtual network that you define

### Installation Guide

1. Download the template [vpc-2azs.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/vpc/vpc-2azs.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `vpc-2azs.json`.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## VPC with private and public subnets in three Availability Zones

Use the `vpc-3azs.json` template to create a VPC with three private and two public subnets.

### Architecture

![Architecture](./vpc-3azs.png?raw=true "Architecture")

### Components

#### AWS services

* VPC: virtual network that you define

### Installation Guide

1. Download the template [vpc-3azs.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/vpc/vpc-3azs.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `vpc-3azs.json`.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## NAT Gateway

Use the `vpc-nat-gateway.json` template to create a NAT Gateway that forwards HTTP, HTTPS and NTP traffic from private subnets to the Internet.

### Architecture

![Architecture](./vpc-nat-gateway.png?raw=true "Architecture")

### Components

#### AWS services

* EC2: NAT Gateway
* Auto Scaling: manages the fleet of virtual machines

### Installation Guide

1. Download the template [vpc-nat-gateway.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/vpc/vpc-nat-gateway.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `vpc-nat-gateway.json`.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## NAT instance

Use the `vpc-nat-instance.json` template to create a **highly available** NAT instance that forwards HTTP, HTTPS and NTP traffic from private subnets to the Internet.

### Architecture

![Architecture](./vpc-nat-instance.png?raw=true "Architecture")

### Components

#### AWS services

* EC2: virtual machine used as the NAT instance
* Auto Scaling: manages the fleet of virtual machines

### Installation Guide

1. Download the template [vpc-nat-instance.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/vpc/vpc-nat-instance.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `vpc-nat-instance.json`.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Support needed?

Do you need help?

Andreas Wittig: [![Contact Andreas on Codementor](https://cdn.codementor.io/badges/contact_me_github.svg)](https://www.codementor.io/andreaswittig)

Michael Wittig: [![Contact Andreas on Codementor](https://cdn.codementor.io/badges/contact_me_github.svg)](https://www.codementor.io/andreaswittig)


--------

[AWS diagrams created with Cloudcraft](https://cloudcraft.co/)
