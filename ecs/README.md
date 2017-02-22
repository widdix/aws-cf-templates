# ECS Templates for AWS CloudFormation

[EC2 Container Service (ECS)](https://aws.amazon.com/ecs/) is a highly scalable, fast, container management service that makes it easy to run, stop, and manage Docker containers on a cluster of Amazon EC2 instances. To run an application on ECS you need the following components:

* Docker image published to [Docker Hub](https://hub.docker.com/) or [EC2 Container Registry (ECR)](https://aws.amazon.com/ecr/)
* ECS cluster
* ECS service referencing an ECS task definition

We provide you templates for the ECS cluster and the service. We also provide you with a way to create a task definition. You need to publish the Docker image.

## ECS cluster
This template describes a fault tolerant and scalable ECS cluster on AWS.

![Architecture](./cluster.png?raw=true "Architecture")

### Installation Guide
1. This templates depends on our [`vpc-*azs.yaml`](../vpc/) template. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=vpc-2azs&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/vpc/vpc-2azs.yaml">Launch Stack</a>
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=ecs-cluster&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/ecs/cluster.yaml">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

### Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `vpc/vpc-ssh-bastion.yaml`
* `security/auth-proxy-*.yaml`

## ECS service
This template describes a fault tolerant and scalable ECS service on AWS.

### Creating an ECS task definition
Before you can start with the ECS service, you need to create a task definition. The task definition references your Docker image from Docker Hub or ECR.

In the [container-definitions.json](./container-definitions.json) file, replace:
* `$Image` with your published Docker image (e.g. `nginx:1.11.5` or `123456789012.dkr.ecr.us-east-1.amazonaws.com/demo:1.0.0`)
* `$AWSRegion` with the region your ECS cluster runs in (e.g. `eu-west-1`)
* `$ClusterLogGroup` with the `LogGroup` output from the `ecs-cluster` stack (e.g. via the CLI `aws cloudformation describe-stacks --stack-name $ClusterName --query "Stacks[0].Outputs[?OutputKey=='LogGroup'].OutputValue" --output text`)
* `$ServiceName` with the name of the service (e.g. `demo`)

Other options can be found in the AWS docs: http://docs.aws.amazon.com/cli/latest/reference/ecs/

The following CLI command creates a task definition and outputs the unique ARN that you will need later when you create the service:

```
aws ecs register-task-definition --family $ServiceName --network-mode bridge --container-definitions file://container-definitions.json --query "taskDefinition.taskDefinitionArn" --output text
```

#### Updating an ECS task definition

If you want to update your task definition because you want to deploy a new version of your image, just re run the `aws ecs register-task-definition` command from above. This will create a new task definition because you can not change them. Take a note of the new ARN that the command returns.


### Choosing a service template flavour
We provide two service templates.
The first one (`service-cluster-alb.yaml`) uses the cluster's load balancer and path based routing. If you want to run multiple services on the same cluster they all will use the same domain name but start with different paths (e.g. `https://yourdomain.com/service1/` and `https://yourdomain.com/service2/`).
The second one (`service-dedicated-alb.yaml`) includes a dedicated load balancer (ALB). You can then use a separate domain name for each service.

### Using the cluster's load balancer and path based routing
This template describes a fault tolerant and scalable ECS service that uses the cluster's load balancer and path based routing.

![Architecture](./service-cluster-alb.png?raw=true "Architecture")

#### Installation Guide
1. This templates depends on our [`cluster.yaml`](../ecs/) template. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=ecs-cluster&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/ecs/cluster.yaml">Launch Stack</a>
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=ecs-service&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/ecs/service-cluster-alb.yaml">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

#### Dependencies
* `ecs/cluster.yaml` (**required**)

### Using a dedicated load balancer for the service
This template describes a fault tolerant and scalable ECS service that uses a dedicated load balancer for the service.

![Architecture](./service-dedicated-alb.png?raw=true "Architecture")

#### Installation Guide
1. This templates depends on our [`cluster.yaml`](../ecs/) template. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=ecs-cluster&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/ecs/cluster.yaml">Launch Stack</a>
1. <a href="https://console.aws.amazon.com/cloudformation/home#/stacks/new?stackName=ecs-service&templateURL=https://s3-eu-west-1.amazonaws.com/widdix-aws-cf-templates/ecs/service-dedicated-alb.yaml">Launch Stack</a>
1. Click **Next** to proceed with the next step of the wizard.
1. Specify a name and all parameters for the stack.
1. Click **Next** to proceed with the next step of the wizard.
1. Click **Next** to skip the **Options** step of the wizard.
1. Check the **I acknowledge that this template might cause AWS CloudFormation to create IAM resources.** checkbox.
1. Click **Create** to start the creation of the stack.
1. Wait until the stack reaches the state **CREATE_COMPLETE**

#### Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `ecs/cluster.yaml` (**required**)

## Support
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [hello@widdix.de](mailto:hello@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
