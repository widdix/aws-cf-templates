# ECS Templates for AWS CloudFormation

Find the documentation here: https://templates.cloudonaut.io/en/stable/ecs/

## Developer notes

### ECS RegionMap
To update the region map execute the following lines in your terminal:

```
for region in $(aws ec2 describe-regions --query 'Regions[].RegionName' --output text); do ami=$(aws --region $region ssm get-parameter --name /aws/service/ecs/optimized-ami/amazon-linux-2/recommended/image_id --query Parameter.Value --output text); printf "'$region':\n  ECSAMI: '$ami'\n"; done
```
