A VPC is a virtual network inside AWS where you can isolate your setup using private IP addresses. A VPC consists of several subnets. Each subnet is bound to an Availability Zone. A **public** subnet has a direct route to the Internet. As long as your EC2 instances have an public IP they can communicate (in and out) with the Internet. A **private** subnet does not have a route to the Internet. Instances in private subnets can not be accessed from the public Internet. If you want to access the Internet from a private subnet you need to create a NAT gateway/instance. You can deploy a bastion host/instance to reduce the attack surface of internal applications.

# VPC with private and public subnets in two Availability Zones
This template describes a VPC with two private and two public subnets.

![Architecture](./img/vpc-2azs.png)

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

If you have an existing VPC you can wrap it into our required form using a legacy VPC wrapper: [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs-legacy.yaml) 

# VPC with private and public subnets in three Availability Zones
This template describes a VPC with three private and three public subnets.

![Architecture](./img/vpc-3azs.png)

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-3azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-3azs.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

If you have an existing VPC you can wrap it into our required form using a legacy VPC wrapper: [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-3azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-3azs-legacy.yaml) 

# VPC with private and public subnets in four Availability Zones
This template describes a VPC with four private and four public subnets.

## Installation Guide
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-4azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-4azs.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

If you have an existing VPC you can wrap it into our required form using a legacy VPC wrapper: [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-4azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-4azs-legacy.yaml) 

# NAT Gateway
This template describes a NAT Gateway that forwards HTTP, HTTPS and NTP traffic from private subnets to the Internet.

![Architecture](./img/vpc-nat-gateway.png)

## Installation Guide
1. This templates depends on one of our `vpc-*azs.yaml` templates. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-nat-gateway&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-nat-gateway.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Dependencies
* `vpc/vpc-*azs.yaml` (**required**)

## Limitations
* The NAT Gateway is a single point of failure because it runs only in one Subnet (and therefore in one Availability Zone): https://github.com/widdix/aws-cf-templates/issues/65

# NAT instance
This template describes a **highly available** Network Address Translation (NAT) instance that forwards HTTP, HTTPS and NTP traffic from private subnets to the Internet.

![Architecture](./img/vpc-nat-instance.png)
## Installation Guide
1. This templates depends on one of our `vpc-*azs.yaml` templates. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-nat-instance&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-nat-instance.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `vpc/vpc-ssh-bastion.yaml` (recommended)
* `operations/alert.yaml` (recommended)

## Limitations
* Only one EC2 instance is managed by the ASG. In case of an outage the instance will be replaced within 5 minutes.

# SSH bastion host/instance
This template describes a **highly available** SSH bastion host/instance. SSH Port 22 is open to the world. You can enable the default ec2-user access protected by the referenced EC2 KeyPair. You can also enable personalized SSH access by using the IAM users and their configured public keys. Use `ssh -A user@ip` to enable forwarding of the authentication agent connection when connection to the bastion host.
**Users are not able to sudo on the bastion host/instance! That's very important for security. Why? SSH places a SSH_AUTH_SOCK file into the /tmp directoy only accessible by the user. If you have root you could use any of those files and jump to other machines as another user!**

![Architecture](./img/vpc-ssh-bastion.png)
## Installation Guide
1. This templates depends on one of our `vpc-*azs.yaml` templates. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-ssh-bastion&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-ssh-bastion.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `operations/alert.yaml` (recommended)

## Limitations
* Only one EC2 instance is managed by the ASG. In case of an outage the instance will be replaced within 5 minutes.

# VPC Endpoint to S3
This template describes a VPC endpoint to securely route traffic within a VPC for private instances to access S3 without the need of a NAT Gateway, NAT instance, or public internet. Refer to [AWS VPC endpoint](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/vpc-endpoints.html) documentation if this is necessary for your stack. By default, access to all S3 actions and buckets is allowed, but may be constrained with a policy document.

![Architecture](./img/vpc-endpoint-s3.png)
## Installation Guide
1. This templates depends on one of our `vpc-*azs.yaml` templates. [Launch Stack](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. [Launch Stack](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-endpoint-s3&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-endpoint-s3.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

# VPC Flow Logs to CloudWatch Logs
This template enables [Flow Log](http://docs.aws.amazon.com/AmazonVPC/latest/UserGuide/flow-logs.html) for the specified VPC. Flow Logs contain aggregated network traffic data in your VPC.

![Architecture](./img/vpc-endpoint-s3.png)
## Installation Guide
1. This templates depends on one of our `vpc-*azs.yaml` templates. [Launch Stack](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. [Launch Stack](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-endpoint-s3&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-flow-logs.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

Flow Logs will show up in CloudWatch Logs a few minutes after activation.
