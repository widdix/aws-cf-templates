# ECS Templates for AWS CloudFormation

Find the documentation here: https://templates.cloudonaut.io/en/stable/ecs/

## Developer notes

### ECS RegionMap
To update the region map execute the following lines in your terminal:

```
for region in $(aws ec2 describe-regions --query "Regions[].RegionName" --output text); do ami=$(aws --region $region ec2 describe-images --filters "Name=name,Values=amzn2-ami-ecs-hvm-2.0.20210708-x86_64-ebs" --query "Images[0].ImageId" --output "text"); printf "'$region':\n  ECSAMI: '$ami'\n"; done
```
