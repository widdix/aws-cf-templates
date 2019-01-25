# WordPress Templates for AWS CloudFormation

Find the documentation here: https://templates.cloudonaut.io/en/stable/wordpress/

## Developer notes

### RegionMap
To update the region map execute the following lines in your terminal:

```
$ regions=$(aws ec2 describe-regions --query "Regions[].RegionName" --output text)
$ for region in $regions; do ami=$(aws --region $region ec2 describe-images --filters "Name=name,Values=amzn2-ami-hvm-2.0.20190115-x86_64-gp2" --query "Images[0].ImageId" --output "text"); printf "'$region':\n  AMI: '$ami'\n"; done
```

### Load Test

[Install k6](https://k6.readme.io/docs).

Replace `BASE_URL` with the URL to your infrastructure.

```
BASE_URL=https://wordpresstest.widdix.de k6 run loadtest.js 
```
