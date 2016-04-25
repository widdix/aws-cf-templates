var AWS = require('aws-sdk');
var response = require('cfn-response');
var iam = new AWS.IAM();
exports.handler = function(event, context) {
  console.log('Invoke: ' + JSON.stringify(event));
  function cb(err) {
    if (err) {
      console.log('Error: ' + JSON.stringify(err));
      response.send(event, context, response.FAILED, {});
    } else {
      response.send(event, context, response.SUCCESS, {});
    }
  }
  if (event.RequestType === 'Delete') {
    iam.deleteAccountPasswordPolicy({}, cb);
  } else if (event.RequestType === 'Create' || event.RequestType === 'Update') {
    iam.updateAccountPasswordPolicy({
      AllowUsersToChangePassword: true,
      HardExpiry: false,
      MaxPasswordAge: 90,
      MinimumPasswordLength: 6,
      PasswordReusePrevention: 6,
      RequireLowercaseCharacters: true,
      RequireNumbers: true,
      RequireSymbols: true,
      RequireUppercaseCharacters: true
    }, cb);
  } else {
    context.fail(new Error('unsupported RequestType: ' + event.RequestType));
  }
};
