# WordPress Templates for AWS CloudFormation

Find the documentation here: https://templates.cloudonaut.io/en/stable/wordpress/

## Developer notes

### RegionMap
To update the region map execute the following lines in your terminal:

```
for region in $(aws ec2 describe-regions --query 'Regions[].RegionName' --output text); do ami=$(aws --region $region ssm get-parameter --name /aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-x86_64-gp2 --query Parameter.Value --output text); prefix_cf=$(aws --region $region ec2 describe-managed-prefix-lists --filters 'Name=prefix-list-name,Values=com.amazonaws.global.cloudfront.origin-facing' --query "PrefixLists[0].PrefixListId" --output "text"); printf "'$region':\n  AMI: '$ami'\n  PrefixListCloudFront: '$prefix_cf'\n"; done
```

### Load Test

[Install k6](https://k6.io/docs/).

Replace `BASE_URL` with the URL to your infrastructure.

```
BASE_URL=https://wordpresstest.widdix.de k6 run loadtest.js 
```
