# ECS Templates for AWS CloudFormation

TODO work in progress

## Creating a ECS cluster

```
export AWS_DEFAULT_REGION=eu-west-1
export ParentVPCStack=vpc-2azs
export ParentSSHBastionStack=vpc-ssh-bastion
export ClusterName=ecs-cluster

aws cloudformation create-stack --stack-name $ClusterName --template-body file://cluster.yaml --parameters ParameterKey=ParentVPCStack,ParameterValue=$ParentVPCStack ParameterKey=ParentSSHBastionStack,ParameterValue=$ParentSSHBastionStack --capabilities CAPABILITY_IAM
aws cloudformation wait stack-create-complete --stack-name $ClusterName

export ClusterLogGroup=$(aws cloudformation describe-stacks --stack-name $ClusterName --query "Stacks[0].Outputs[?OutputKey=='LogGroup'].OutputValue" --output text)
```

## Creating a ECS task definition

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

### Updating a task definition

Just re run the `register-task-definition` command which creates a new task definition.

## Creating a ECS service

### Using the cluster's load balancer and bath based routing

```
aws cloudformation create-stack --stack-name $ClusterName-$ServiceName --template-body file://service.yaml --parameters ParameterKey=ParentVPCStack,ParameterValue=$ParentVPCStack ParameterKey=ParentClusterStack,ParameterValue=$ClusterName "ParameterKey=ClusterLoadBalancerPathPattern,ParameterValue=/$ServiceName/*" ParameterKey=TaskDefinitionArn,ParameterValue=$TaskDefinitionArn --capabilities CAPABILITY_IAM
aws cloudformation wait stack-create-complete --stack-name $ClusterName-$ServiceName
```

### Using a dedicated load balancer for the service

```
aws cloudformation create-stack --stack-name $ClusterName-$ServiceName --template-body file://service.yaml --parameters ParameterKey=ParentVPCStack,ParameterValue=$ParentVPCStack ParameterKey=ParentClusterStack,ParameterValue=$ClusterName ParameterKey=LoadBalancerMode,ParameterValue=Service ParameterKey=TaskDefinitionArn,ParameterValue=$TaskDefinitionArn --capabilities CAPABILITY_IAM
aws cloudformation wait stack-create-complete --stack-name $ClusterName-$ServiceName
```

### Updating a ECS service

If you want to release a new task definition, this is the way to go:

```
aws cloudformation update-stack --stack-name $ClusterName-$ServiceName --template-body file://service.yaml --parameters ParameterKey=ParentVPCStack,UsePreviousValue=true ParameterKey=ParentClusterStack,UsePreviousValue=true ParameterKey=LoadBalancerMode,UsePreviousValue=true ParameterKey=ClusterLoadBalancerPriority,UsePreviousValue=true ParameterKey=ClusterLoadBalancerPathPattern,UsePreviousValue=true ParameterKey=ServiceLoadBalancerELBScheme,UsePreviousValue=true ParameterKey=TaskDefinitionArn,ParameterValue=$TaskDefinitionArn ParameterKey=DesiredCount,UsePreviousValue=true ParameterKey=ContainerPort,UsePreviousValue=true --capabilities CAPABILITY_IAM
# The next command can take up to 5 minutes because of instance draining (http://docs.aws.amazon.com/AmazonECS/latest/developerguide/container-instance-draining.html)
aws cloudformation wait stack-update-complete --stack-name $ClusterName-$ServiceName
```
