# Developer notes

## RegionMap
To update the region map execute the following lines in your terminal:

```
$ regions=$(aws ec2 describe-regions --query "Regions[].RegionName" --output text)
$ for region in $regions; do ami=$(aws --region $region ec2 describe-images --filters "Name=name,Values=amzn-ami-vpc-nat-hvm-2015.09.1.x86_64-ebs" --query "Images[0].ImageId" --output "text"); echo "\"$region\": {\"NATAMI\": \"$ami\"},"; done
```
