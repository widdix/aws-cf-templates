# ECS Templates for AWS CloudFormation

Find the documentation here: http://templates.cloudonaut.io/en/stable/ecs/

## Developer notes

### ECS RegionMap
To update the region map execute the following lines in your terminal:

```
$ regions=$(aws ec2 describe-regions --query "Regions[].RegionName" --output text)
$ for region in $regions; do ami=$(aws --region $region ec2 describe-images --filters "Name=name,Values=amzn-ami-2017.09.g-amazon-ecs-optimized" --query "Images[0].ImageId" --output "text"); printf "'$region':\n  ECSAMI: '$ami'\n"; done
```
