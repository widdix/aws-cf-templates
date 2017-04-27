# EC2 with auto-recovery
This template describes an EC2 instance with auto-recovery. If the instance fails it will be replaced automatically. All data stored on EBS volumes will still be available. The public and private IP address won't change. Auto-recovery does only work inside of a single availability zone (AZ).

![Architecture](./img/ec2-auto-recovery.png)

## Installation Guide
1. This templates depends on one of our [`vpc-*azs.yaml`](../vpc/) templates. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/vpc/vpc-2azs.yaml)
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=ec2-auto-recovery&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/ec2/ec2-auto-recovery.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Grab the public `IPAddress` of the EC2 instance from the **Outputs** tab of your stack.

## Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `vpc/vpc-ssh-bastion.yaml` (recommended)
* `operations/alert.yaml` (recommended)

## Limitations
* The EC2 instance only runs in a single AZ. In case of an AZ outage the instance will be unavailable.
