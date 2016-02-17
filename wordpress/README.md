# aws-cf-templates

## Wordpress

Use the `wordpress-ha.json` template to create a **fault tolerant** and **scalable** Wordpress environment within minutes.

### Architecture

![Architecture](./wordpress-ha.png?raw=true "Architecture")

### Components

#### Dependencies

This template depends on other templates.

* [VPC stack with at least two public and private subnets](https://github.com/widdix/aws-cf-templates/tree/master/vpc)

#### AWS services

* EC2: virtual servers running Apache serving Wordpress (PHP application)
* RDS: highly available MySQL database
* S3: stores and delivers uploaded media files (e.g. images, videos, ...)
* ELB: distributes incoming HTTP requests to multiple virtual servers
* VPC: environment is running in a separate private network
* Auto Scaling: manages the fleet of virtual servers
* CloudWatch: monitors the usage of the virtual servers and adds or removes additional servers if needed

#### Others

* Wordpress plugins: amazon-web-services and amazon-s3-and-cloudfront
* Wordpress CLI: wp-cli 

### Installation Guide

1. This templates depends on one of our VPC templates. [Please create a VPC stack with at least two public and private subnets first.](https://github.com/widdix/aws-cf-templates/tree/master/vpc)
1. Download the template [wordpress-ha.json](https://raw.githubusercontent.com/widdix/aws-cf-templates/master/wordpress/wordpress-ha.json)
1. Open AWS CloudFormation within the Management Console: [https://console.aws.amazon.com/cloudformation](https://console.aws.amazon.com/cloudformation).
1. Create a new stack by clicking on the **Create Stack** button.
1. Select **Upload a template to Amazon S3** and upload the template `wordpress-ha.json`.
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

## Support needed?

Do you need help? Mail to [team@widdix.de](mailto:team@widdix.de).
