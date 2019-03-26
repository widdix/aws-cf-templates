#!/bin/bash -ex

TEMP_PATH=.temp
RELEASE_ZIP=layer.zip

# Copy sources to temporary folder
rm -r "$TEMP_PATH" 2>/dev/null || true
mkdir -p "$TEMP_PATH"
cp -R *.js *.json "$TEMP_PATH/"
cd "$TEMP_PATH"

# Remove aws-sdk, already installed on Lambda
npm uninstall aws-sdk
# Install dependencies
npm ci --production
# Package artifact
rm "../$RELEASE_ZIP" 2>/dev/null || true
zip -r "../$RELEASE_ZIP" .

# Cleanup
cd ..
rm -r "$TEMP_PATH"

# publish (region)
publish () {
  echo "publish ${1}"
  version=$(aws lambda publish-layer-version --layer-name aws-cf-templates-lambda-drain-instance --zip-file fileb://layer.zip --query Version --output text --region ${1})
  aws lambda add-layer-version-permission --layer-name aws-cf-templates-lambda-drain-instance --version-number ${version} --statement-id "aws-cf-templates" --action "lambda:GetLayerVersion" --principal "*" --region ${1}
}

publish ap-northeast-1
publish ap-northeast-2
publish ap-south-1
publish ap-southeast-1
publish ap-southeast-2
publish ca-central-1
publish eu-central-1
publish eu-north-1
publish eu-west-1
publish eu-west-2
publish eu-west-3
publish sa-east-1
publish us-east-1
publish us-east-2
publish us-west-1
publish us-west-2

rm layer.zip
