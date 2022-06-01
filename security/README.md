# Security Templates for AWS CloudFormation

Find the documentation here: https://templates.cloudonaut.io/en/stable/security/

## Developer notes

### RegionMap
To update the region map execute the following lines in your terminal:

```
for region in $(aws ec2 describe-regions --query "Regions[].RegionName" --output text); do ami=$(aws --region $region ssm get-parameters --names /aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-x86_64-gp2 --query "Parameters[0].Value" --output "text"); printf "'$region':\n  AMI: '$ami'\n"; done
```
