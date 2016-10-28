# Wordpress Templates for AWS CloudFormation

## Wordpress: fault tolerant and scalable
Use this CloudFormation template to create a fault tolerant and scalable Wordpress environment on AWS.

### Features
* HTTPS only
* CDN caching static files
* Scalable file storage
* Fault tolerance due to multi-AZ setup

### Services
This template combines the following services:
* CloudFront: CDN for dynamic and static content
* ELB: load balancer forwarding requests to EC2 instances and terminating SSL
* EC2: virtual machines running the web servers
* EFS: storage for Wordpress files (Wordpress core, plugins, themes, user uploads, ...)
* RDS: MySQL database

![Architecture](./wordpress-ha.png?raw=true "Architecture")

## Installation Guide
1. This templates depends on our [`vpc-2azs.yaml`](../vpc/) template. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-2azs.yaml">Launch Stack</a>
1. Create an ACM certificate for your domain name within the region you want to launch your stack in. Copy the ARN of the certificate.
1. Create another ACM certificate for your domain in region `us-east-1`. Copy the ARN of the certificate.
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=wordpress-ha&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/wordpress/wordpress-ha.yaml">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Grab the `URL` of the Wordpress environment from the **Outputs** tab of your stack.

## Dependencies
* `vpc/vpc-*azs.yaml` (**required, Wordpress will use 2 AZs only**)
* `vpc/vpc-ssh-bastion.yaml` (optional)
* `security/auth-proxy-*.yaml` (optional)

## Limitations
* PHP files are cached for 300 seconds on the web servers.
* Static files `wp-includes` and `wp-content` are cached for 15 minutes on the CDN.

## Support
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [team@widdix.de](mailto:team@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).