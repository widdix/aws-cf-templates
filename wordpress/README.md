# WordPress Templates for AWS CloudFormation

Find the documentation here: http://templates.cloudonaut.io/en/stable/wordpress/

## Developer notes

### RegionMap
To update the region map execute the following lines in your terminal:

```
$ regions=$(aws ec2 describe-regions --query "Regions[].RegionName" --output text)
$ for region in $regions; do ami=$(aws --region $region ec2 describe-images --filters "Name=name,Values=amzn-ami-hvm-2017.03.1.20170812-x86_64-gp2" --query "Images[0].ImageId" --output "text"); printf "'$region':\n  AMI: '$ami'\n"; done
```

### Load Test

[Install k6](https://k6.readme.io/docs).

Replace `BASE_URL` with the URL to your infrastructure.

```
BASE_URL=https://wordpresstest.widdix.de k6 run loadtest.js 
```

## Premium Support
We offer Premium Support for our CloudFormation templates: setting up environments based on our templates, adopting templates to specific use cases, resolving issues in production environments. [Hire us!](https://widdix.net/)

## About
A [cloudonaut.io](https://cloudonaut.io/templates-for-aws-cloudformation/) project. Engineered by [widdix](https://widdix.net).
