#!/usr/bin/env bash
set -euo pipefail

IFS=$'\n\t'

#####
# TODO
#       + Validate
#       + Destroy
#####

# See: http://wiki.bash-hackers.org/scripting/debuggingtips#making_xtrace_more_useful
export PS5='+(${BASH_SOURCE}:${LINENO}): ${FUNCNAME[0]:+${FUNCNAME[0]}(): }'

# Absolute path to current dir.
HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

declare vpc_stack_name="jenkins-vpc"

_usage() {
    echo "USAGE:"
    echo -e "\tAWS_PROFILE=my_aws_profile_name $1"
}

_generate_ssh_key() {
    : # ssh-keygen -t rsa -b 4096 -C "spargo-aws-control@spargoinc.com"
}

declare AWS_PROFILE=${AWS_PROFILE:-}
if [[ -z $AWS_PROFILE ]]; then
    echo "You must specify your AWS profile name"
    echo "See: https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html"
    _usage
    exit 255
fi
echo "Using AWS profile ${AWS_PROFILE}"

_get_account_id() {
    aws sts \
        get-caller-identity \
            --query Account \
            --output text
}

# Create s3 bucket if it doesn't exist.
# This bucket will hold our templates so that CloudFormation can execute them.
# You can't omit the bucket using CLI because the template size exceeds the limit set by AWS CLI (51,200 bytes), so uploading works best if you want to do this via terminal.
_ensure_s3_bucket() {
    local account_id
    local bucket_name

    bucket_name="jenkins-cf-templates-$(_get_account_id)"

    aws s3 ls s3://${bucket_name} > /dev/nul 2>&1 || aws s3 mb s3://${bucket_name} > /dev/nul 2>&1
    echo "${bucket_name}"
}

# Deploy VPC
_cf_deploy_vpc() {
    local bucket=$1

    set -x
    # Create the VPC structure.
#    (
        aws cloudformation deploy \
            --template-file "${HERE}/../vpc/vpc-2azs.yaml" \
            --capabilities CAPABILITY_IAM \
            --s3-bucket $bucket \
            --stack-name ${vpc_stack_name} # > /dev/null & \
#    ) && watch \
#        "aws cloudformation describe-stack-events \
#            --stack-name $vpc_stack_name | \
#                jq -r '.StackEvents[] |
#                        \"\\(.Timestamp | sub(\"\\\\.[0-9]+Z$\"; \"Z\") | fromdate | strftime(\"%H:%M:%S\") ) \\(.LogicalResourceId) \\(.ResourceType) \\(.ResourceStatus)\"
#                ' | column -t"
    set +x
}

_get_my_ip() {
    dig +short myip.opendns.com @resolver1.opendns.com
}

# Deploy all other Jenkins resources (EC2, ELB, etc).
_cf_deploy_jenkins() {
    local bucket=$1
    local stack="jenkins-resources"

    set -x
    #####
    # TODO:
    #   These arguments must be inspected before the task is complete.
    #####
    # Create all other Jenkins resources.
    # Credits for watching events as the occur: https://advancedweb.hu/cloudformation-cli-workflows/#deploy-and-watch-the-events
#    (
        aws cloudformation deploy \
            --template-file "${HERE}/jenkins2-ha-agents.yaml" \
            --capabilities CAPABILITY_IAM \
            --s3-bucket $bucket \
            --parameter-overrides \
            CIDRWhiteList="$(_get_my_ip)/32" \
                KeyName="spargo-control" \
                MasterAdminPassword="mypassword" \
                ParentVPCStack="${vpc_stack_name}" \
            --stack-name $stack # > /dev/null & \
#    ) && watch \
#        "aws cloudformation describe-stack-events \
#            --stack-name $stack | \
#                jq -r '.StackEvents[] |
#                        \"\\(.Timestamp | sub(\"\\\\.[0-9]+Z$\"; \"Z\") | fromdate | strftime(\"%H:%M:%S\") ) \\(.LogicalResourceId) \\(.ResourceType) \\(.ResourceStatus)\"
#                ' | column -t"
    set +x
}

main() {
    local bucket_name

    # echo "Creating #3 bucket if it doesnt exist..."
    bucket_name="$(_ensure_s3_bucket)"
    # echo "S3 bucket '${bucket_name} created."

    # echo "Deploying VPC"
    # _cf_deploy_vpc ${bucket_name}

    echo "Deploying all Jenkins resources"
    _cf_deploy_jenkins ${bucket_name}
}

main $@
exit $?
