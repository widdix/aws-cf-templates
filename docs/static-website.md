<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: Manage Free Templates for AWS CloudFormation with the [widdix CLI](./cli/)

# Static website with CDN
This template describes the infrastructure for hosting a static website over HTTPS behind a CDN.

![Architecture](./img/static-website.png)

## Index Document / Default Root Object

S3 and CloudFront behave differently when it comes to index document support in subdirectories. You might expect that if you have a subdirectory (e.g. `folder`) with a `index.html` file inside, that you get the file if you make q HTTP request to `/folder/`. Unfortunately, that's not the default behavior of CloudFront but there is a workaround using Lambda@Edge.

### S3 Website Hosting behavior with Index Document (not provided by the template!)



## Installation Guide

1. Switch to the us-east-1 (N. Virginia) region.
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home?region=us-east-1#/stacks/new?stackName=lambdaedge-index-document&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/static-website/lambdaedge-index-document.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**
1. Copy the `LambdaVersionArn` output of the stack to your clipboard.
1. Switch to the region where you want to S3 bucket with static files to be created in.
1. [![Launch Stack](./img/launch-stack.png)](https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=static-website&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates-releases-eu-west-1/__VERSION__/static-website/static-website.yaml)
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
    a. Set the `LambdaEdgeSubdirectoriesVersionArn` to the value of the `LambdaVersionArn` output. 
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

## Dependencies
* `static-website/lambdaedge-index-document.yaml` (**required**)
* `vpc/zone-*.yaml`
