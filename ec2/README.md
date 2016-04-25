# aws-cf-templates

## EC2 with auto-recovery

Use the `ec2-auto-recovery.json` template to launch an EC2 instance with auto-recovery. If the instance fails it will be replaced automatically. All data stored on EBS volumes will still be available. The public and private IP address won't change. Auto-recovery does only work inside of a single availability zone (AZ).

### Architecture

![Architecture](./ec2-auto-recovery.png?raw=true "Architecture")

### Components

#### AWS services

* EC2: virtual server
* CloudWatch: monitors the EC2 instance and triggers auto-recovery if necessary

### Installation Guide

1. Download the template [ec2-auto-recovery.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/ec2/ec2-auto-recovery.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `ec2-auto-recovery.json`.
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Grab the public IP address of the EC2 instance from the **Outputs** tab of your stack.

## Support needed?

Do you need help?

Andreas Wittig: [![Contact Andreas on Codementor](https://cdn.codementor.io/badges/contact_me_github.svg)](https://www.codementor.io/andreaswittig)

Michael Wittig: [![Contact Andreas on Codementor](https://cdn.codementor.io/badges/contact_me_github.svg)](https://www.codementor.io/andreaswittig)
