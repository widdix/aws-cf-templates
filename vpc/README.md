# VPC Templates for AWS CloudFormation

Find the documentation here: https://templates.cloudonaut.io/en/stable/vpc/

## Developer notes

### RegionMap
To update the region map execute the following lines in your terminal:

#### AMI
```
for region in $(aws ec2 describe-regions --query 'Regions[].RegionName' --output text); do ami=$(aws --region $region ssm get-parameter --name /aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-x86_64-gp2 --query Parameter.Value --output text); printf "'$region':\n  AMI: '$ami'\n"; done
```

#### NATAMI
```
for region in $(aws ec2 describe-regions --query 'Regions[].RegionName' --output text); do ami=$(aws --region $region ec2 describe-images --filters 'Name=name,Values=amzn-ami-vpc-nat-2018.03.0.20230322.0-x86_64-ebs' --query 'Images[0].ImageId' --output text); printf "'$region':\n  NATAMI: '$ami'\n"; done
```
