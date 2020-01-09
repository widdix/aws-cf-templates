# Jenkins Templates for AWS CloudFormation

Find the documentation here: https://templates.cloudonaut.io/en/stable/jenkins/

## Developer notes

### RegionMap
To update the region map execute the following lines in your terminal:

```
$ regions=$(aws ec2 describe-regions --query "Regions[].RegionName" --output text)
$ for region in $regions; do ami=$(aws --region $region ec2 describe-images --filters "Name=name,Values=amzn2-ami-hvm-2.0.20191116.0-x86_64-gp2" --query "Images[0].ImageId" --output "text"); printf "'$region':\n  AMI: '$ami'\n"; done
```
## Deploying

1. Deploy the VPC
The VPC is a dependency of the Jenkins stack.

    aws cloudformation deploy \
        --profile <IAM_USER_PROFILE> \
        --capabilities CAPABILITY_IAM \
        --s3-bucket "unmanaged-cf-templates-<ACCOUNT_ID>" \
        --stack-name "<NAME_OF_THE_VPC_STACK>" \
        --template-file "./../vpc/vpc-2azs.yaml" \

2. Deploy the Jenkins stack.
*Note*: this will set the CIDR whitelist to the current public IPv4 address of the workstation where the `aws cloudformation deploy` is run.

    aws cloudformation deploy \
        --profile <IAM_USER_PROFILE> \
        --capabilities CAPABILITY_IAM \
        --parameter-overrides \
            AgentEnableMetrics="<true|false>" \
            CIDRWhiteList="$(dig +short myip.opendns.com @resolver1.opendns.com)/32" \
            KeyName="<SSH_KEYPAIR_NAME>" \
            MasterAdminPassword="admin_password" \
            MasterEnableMetrics="true" \
            ParentVPCStack="<NAME_OF_THE_VPC_STACK>" \
        --s3-bucket "unmanaged-cf-templates-templates-<ACCOUNT_ID>" \
        --stack-name "<NAME_OF_THE_JENKINS_STACK>" \
        --template-file "./jenkins2-ha-agents.yaml"
