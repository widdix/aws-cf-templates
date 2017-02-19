# ECS Templates for AWS CloudFormation

TODO work in progress

## ECS cluster
This template describes TODO description

TODO architecture diagram

### Installation Guide
TODO using simple buttons
```
export AWS_DEFAULT_REGION=eu-west-1
export ParentVPCStack=vpc-2azs
export ParentSSHBastionStack=vpc-ssh-bastion
export ClusterName=ecs-cluster

aws cloudformation create-stack --stack-name $ClusterName --template-body file://cluster.yaml --parameters ParameterKey=ParentVPCStack,ParameterValue=$ParentVPCStack ParameterKey=ParentSSHBastionStack,ParameterValue=$ParentSSHBastionStack --capabilities CAPABILITY_IAM
aws cloudformation wait stack-create-complete --stack-name $ClusterName

export ClusterLogGroup=$(aws cloudformation describe-stacks --stack-name $ClusterName --query "Stacks[0].Outputs[?OutputKey=='LogGroup'].OutputValue" --output text)
```

### Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `vpc/vpc-ssh-bastion.yaml`
* `security/auth-proxy-*.yaml`

## ECS service
This template describes TODO description

TODO architecture diagram

### Creating an ECS task definition

```
export ServiceName=demo
```

In `container-definitions.json` replace
* `$AWS_DEFAULT_REGION` with the region in `$AWS_DEFAULT_REGION` (e.g. `eu-west-1`)
* `$ClusterLogGroup` with the `LogGroup` output from the `cluster` stack in `$ClusterLogGroup`
* `$ServiceName` wit the name of the service
Other options can be found in the AWS docs: http://docs.aws.amazon.com/cli/latest/reference/ecs/

```
export TaskDefinitionArn=$(aws ecs register-task-definition --family $ServiceName --network-mode bridge --container-definitions file://container-definitions.json --query "taskDefinition.taskDefinitionArn" --output text)
```

#### Updating

Just re run the `register-task-definition` command from above to create a new task definition.

### Using the cluster's load balancer and bath based routing
TODO description

#### Installation Guide
TODO using simple buttons
```
aws cloudformation create-stack --stack-name $ClusterName-$ServiceName --template-body file://service-cluster-alb.yaml --parameters ParameterKey=ParentClusterStack,ParameterValue=$ClusterName "ParameterKey=LoadBalancerPathPattern,ParameterValue=/$ServiceName/*" ParameterKey=TaskDefinitionArn,ParameterValue=$TaskDefinitionArn --capabilities CAPABILITY_IAM
aws cloudformation wait stack-create-complete --stack-name $ClusterName-$ServiceName
```

##### Updating

If you want to release a new task definition, this is the way to go:

```
aws cloudformation update-stack --stack-name $ClusterName-$ServiceName --template-body file://service-cluster-alb.yaml --parameters ParameterKey=ParentClusterStack,UsePreviousValue=true ParameterKey=LoadBalancerPriority,UsePreviousValue=true ParameterKey=LoadBalancerPathPattern,UsePreviousValue=true ParameterKey=LoadBalancerHttps,UsePreviousValue=true ParameterKey=TaskDefinitionArn,ParameterValue=$TaskDefinitionArn ParameterKey=DesiredCount,UsePreviousValue=true ParameterKey=ContainerPort,UsePreviousValue=true --capabilities CAPABILITY_IAM
# The next command can take up to 5 minutes because of instance draining (http://docs.aws.amazon.com/AmazonECS/latest/developerguide/container-instance-draining.html)
aws cloudformation wait stack-update-complete --stack-name $ClusterName-$ServiceName
```

#### Dependencies
* `ecs/cluster.yaml` (**required**)

### Using a dedicated load balancer for the service
TODO description

#### Installation Guide
TODO using simple buttons
```
aws cloudformation create-stack --stack-name $ClusterName-$ServiceName --template-body file://service-dedicated-alb.yaml --parameters ParameterKey=ParentVPCStack,ParameterValue=$ParentVPCStack ParameterKey=ParentClusterStack,ParameterValue=$ClusterName ParameterKey=TaskDefinitionArn,ParameterValue=$TaskDefinitionArn --capabilities CAPABILITY_IAM
aws cloudformation wait stack-create-complete --stack-name $ClusterName-$ServiceName
```

##### Updating

If you want to release a new task definition, this is the way to go:

```
aws cloudformation update-stack --stack-name $ClusterName-$ServiceName --template-body file://service-dedicated-alb.yaml --parameters ParameterKey=ParentVPCStack,UsePreviousValue=true ParameterKey=ParentClusterStack,UsePreviousValue=true ParameterKey=LoadBalancerScheme,UsePreviousValue=true ParameterKey=LoadBalancerCertificateArn,UsePreviousValue=true ParameterKey=TaskDefinitionArn,ParameterValue=$TaskDefinitionArn ParameterKey=DesiredCount,UsePreviousValue=true ParameterKey=ContainerPort,UsePreviousValue=true --capabilities CAPABILITY_IAM
# The next command can take up to 5 minutes because of instance draining (http://docs.aws.amazon.com/AmazonECS/latest/developerguide/container-instance-draining.html)
aws cloudformation wait stack-update-complete --stack-name $ClusterName-$ServiceName
```

#### Dependencies
* `vpc/vpc-*azs.yaml` (**required**)
* `ecs/cluster.yaml` (**required**)

## Support
We offer support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## Feedback
We are looking forward to your feedback. Mail to [hello@widdix.de](mailto:hello@widdix.de).

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
