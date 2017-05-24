# Migrate from v3 to v4

There is a breaking change in the VPC layout going from v3 to v4. This change enables HA NAT Gateway/Instance (https://github.com/widdix/aws-cf-templates/issues/65). Updating your VPC can will cause connectivity interruptions in `SubnetBPrivate`, `SubnetCPrivate`, and `SubnetDPrivate` until you created new NAT Gateway/Instance for each `SubnetZone` in step 3b.

> None of our templates launch workloads into private subnets that require Internet access. This could only be an issue if you use other workloads.

1. Update VPC stacks with the matching updated template (`vpc/vpc-2azs.yaml`, `vpc/vpc-2azs-legacy.yaml`, `vpc/vpc-3azs.yaml`, `vpc/vpc-3azs-legacy.yaml`, `vpc/vpc-4azs.yaml`, `vpc/vpc-4azs-legacy.yaml`), leave the parameters as they are.
2. Update VPC Endpoint stacks with the matching updated template (`vpc/vpc-endpoint-s3.yaml`), leave the parameters as they are.
3. Update VPC NAT Gateway/Instance stacks
  a. Update VPC NAT Gateway/Instance stacks with the matching updated template (`vpc/vpc-nat-gateway.yaml`, `vpc/vpc-nat-instance.yaml`), set `SubnetZone` parameter to `A`.
  b. If you updated anything in a. create a new VPC NAT Gateway/Instance stack for each missing `SubnetZone` by setting the `SubnetZone` parameter to `B`, `C`, or `D`.
