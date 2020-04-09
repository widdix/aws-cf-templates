<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v4 to v5

There are two smaller breaking changes:

## opertions/backup-dynamodb.yaml

A new mandatory parameter was added: `ParentVPCStack`.

If you want to migrate stacks based on `opertions/backup-dynamodb.yaml`, you have to create a VPC stack based on `vpc/vpc-*azs.yaml` and pass the name of the newly created vpc stack as the parameter `ParentVPCStack`. You can also use an existing vpc stack.

## ecs/cluster.yaml

The auto scaling rules have changed and new parameters where added (all with default values). The cluster now scales based on a new metric: `SchedulableContainers`. Scaling based on SchedulableContainers is described in detail here: http://garbe.io/blog/2017/04/12/a-better-solution-to-ecs-autoscaling/

If you want to migrate stacks based on `ecs/cluster.yaml`, you have to check your ECS task definitions and look for the highest memory and cpu reservation and set the new parameters `ContainerMaxMemory` and `ContainerMaxCPU` to those values.
