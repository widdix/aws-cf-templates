Your source for free [AWS CloudFormation](https://aws.amazon.com/cloudformation/) templates. Bootstrap high-quality AWS infrastructure within minutes.

# Why Free Templates for AWS CloudFormation

* Speed up development and migration: reuse our templates to create complex environments for common use cases with ease.
* Rely on high-quality infrastructure templates: peer-reviewed by an expert (certified AWS solutions architect Professional) and verified with automated tests.
* All templates are production-ready. If no other limitations are documented, the following applies:
    * Highly available: The template has no single point of failure.
    * Scalable: The capacity increases or decreases based on utilization (auto-scaling).
    * Frictionless deployment: You can deploy new versions of the templates or your application without downtime.
    * Secure: We use the latest operating systems and software components. We follow the least privilege principle in all areas (IAM, network). We support encryption. We enable backups.
    * Operator-friendly: Logging, monitoring, and alerting are configured out-of-the-box.

# Related projects

## widdix CLI
[widdix, a CLI tool to manage Free Templates for AWS CloudFormation](./cli/).

## cfn-modules
[Rapid CloudFormation](https://github.com/cfn-modules/docs): Modular, production ready, open source.

# Infrastructure Templates
Choose from our template catalog:

* [Elastic Compute Cloud (EC2)](./ec2/)
* [EC2 Container Service (ECS)](./ecs/)
* [Fargate](./fargate/)
* [Jenkins ](./jenkins/)
* [Operations](./operations/)
* [Security](./security/)
* [State / Data](./state/)
* [Static Website](./static-website/)
* [Virtual Private Cloud (VPC)](./vpc/)
* [WordPress](./wordpress/)

# How does CloudFormation work

CloudFormation turns a template (JSON or YAML) into a stack like the following figure shows.

![CloudFormation](./img/cloudformation.png)

You can apply updates to an existing stack with an updated template. CloudFormation will figure out what needs to be changed.

**Never make manual changes to infrastructure managed by CloudFormation!**

# Example

Imagine you want to set up a Jenkins automation server. This project uses templates as modules to reuse infrastructure components. Some are optional, while others are required. The following figure shows templates that are needed to set up Jenkins.

![Example: Modules](./img/example-modules.png)

If you create a stack, you sometimes have to supply parameters that start with `Parent`. That's the mechanism to pass dependent stacks into a stack.

Let's look at the first dependency.

## VPC dependency (required)

Many templates depend on a VPC stack. The VPC is a required dependency. 

![VPC dependency](./img/example-vpc.png)

## Alert dependency (optional)

If you want to receive alerts when things go wrong, you can optionally supply an alert stack. 

![Alert dependency](./img/example-alert.png)

I highly recommend using an alert stack. Otherwise, you will not know when things go wrong (and they will!).

## SSH bastion host dependency (optional)

If you want to add some extra security, you can use an SSH bastion host.

![SSH bastion host dependency](./img/example-ssh-bastion-host.png)

The bastion host has a optional dependency on the alert stack. So if you want to receive alerts if your bastion hosts is in trouble, supply an alert stack.

## Jenkins

Finally, you can create the Jenkins stack.

![Jenkins](/img/example-jenkins.png)

The cool thing is that you can re-use the dependencies. E.g., you can use the same SSH bastion host for Jenkins and WordPress.

# License
All templates are published under Apache License Version 2.0.

# Sponsors
[Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors.

# Training and Consulting
Do you want to accelerate your start with AWS CloudFormation and our templates? We do offer remote and on-site training for you and your team. Are you looking for guidance on how to use or adapt our templates to your use case? We offer consulting services as well. [Contact us](mailto:hello@widdix.net), and we’ll accelerate your project.

# About
A [cloudonaut.io](https://cloudonaut.io/) project. Engineered by [widdix](https://widdix.net).
