<iframe src="https://ghbtns.com/github-btn.html?user=widdix&repo=aws-cf-templates&type=star&count=true&size=large" frameborder="0" scrolling="0" width="160px" height="30px"></iframe>

> **New**: [Become a sponsor](https://github.com/sponsors/widdix) via GitHub Sponsors!

# Migrate from v3 to v4

There is a breaking change in the VPC layout going from v3 to v4. This change enables HA NAT Gateway/Instance (https://github.com/widdix/aws-cf-templates/issues/65). Updating your VPC will cause connectivity interruptions to the Internet in `SubnetBPrivate`, `SubnetCPrivate`, and `SubnetDPrivate` until you created new NAT Gateway/Instance for each `SubnetZone` in step 3b.

> None of our templates launch workloads into private subnets that require Internet access. This could only be an issue if you run external workloads in a VPC based on our templates.

1. Update VPC stacks with the matching, updated template (`vpc/vpc-2azs.yaml`, `vpc/vpc-2azs-legacy.yaml`, `vpc/vpc-3azs.yaml`, `vpc/vpc-3azs-legacy.yaml`, `vpc/vpc-4azs.yaml`, `vpc/vpc-4azs-legacy.yaml`), leave the parameters as they are.
1. Update VPC Endpoint stacks (if you have one) with the matching, updated template (`vpc/vpc-endpoint-s3.yaml`), leave the parameters as they are.
1. Update VPC NAT Gateway/Instance stacks (if you have one)
    1. Update VPC NAT Gateway/Instance stacks with the matching updated template (`vpc/vpc-nat-gateway.yaml`, `vpc/vpc-nat-instance.yaml`), set `SubnetZone` parameter to `A`.
    1. If you updated anything in a. create a new VPC NAT Gateway/Instance stack for each missing `SubnetZone` by setting the `SubnetZone` parameter to `B`, `C`, or `D`.
