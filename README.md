# Free Templates for AWS CloudFormation
Using our Infrastructure as Code templates will help you to bootstrap common setups on Amazon Web Services (AWS) within minutes. Our templates are exclusively designed for AWS CloudFormation, the Infrastructure as Code services on AWS.

## Benefits
* Speed up development and migration: reuse our CloudFormation templates to create complex environments for common use cases with ease.
* Rely on high quality infrastructure templates: peer-reviewed by an expert (certified AWS solutions architect Professional) and verified with automated tests.
* All templates are production-ready. If no other limitations are documented, they are:
  * Highly available: no single point of failure
  * Scalable: increase or decrease the number of instances based on load
  * Frictionless deployment: deliver new versions of your application automatically without downtime
  * Secure: using the latest operating systems and software components, follow the least privilege principle in all areas
  * Operations: provide tools like logging, monitoring and alerting to recognize and debug problems
* Premium Support available: Get help in case of small and big emergencies and submit feature request.

## Templates
We are offering the following templates:

* [Elastic Compute Cloud (EC2)](./ec2/)
* [EC2 Container Service (ECS)](./ecs/)
* [Jenkins ](./jenkins/)
* [Operations](./operations/)
* [Security](./security/)
* [Static website](./static-website/)
* [Virtual Private Cloud (VPC)](./vpc/)
* [WordPress](./wordpress/)

We are interested in your requirements. [Please use the following survey to upvote and add templates you are impatiently waiting for](https://docs.google.com/forms/d/e/1FAIpQLSerhIuMuCWrHai639FoUOt8ffmMqWr0PWzLhCn3VN29VUi8TA/viewform?usp=send_form)

## Releases
We host all versions starting with version 3.1.0 on the `widdix-aws-cf-templates-releases-eu-west-1` S3 bucket.
The file `vpc/vpc-2azs.yaml` becomes the S3 key `v3.1.0/vpc/vpc-2azs.yaml`.
The latest version can also be found at `latest/vpc/vpc-2azs.yaml`.

The current master branch (work in progress) is hosted on the `widdix-aws-cf-templates` S3 bucket.

## License
All templates are published under Apache License Version 2.0.

## Premium Support
We offer Premium Support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [hello@widdix.de](mailto:hello@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
