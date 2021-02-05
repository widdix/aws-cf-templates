package de.widdix.awscftemplates;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public abstract class AAWSTest extends ATest {

    public final static String IAM_SESSION_NAME = "aws-cf-templates";

    protected final AWSCredentialsProvider credentialsProvider;

    private AmazonEC2 ec2;

    private AmazonIdentityManagement iam;

    protected final AmazonS3 s3;

    private final AWSSecurityTokenService sts;

    public AAWSTest() {
        super();
        if (Config.has(Config.Key.IAM_ROLE_ARN)) {
            final AWSSecurityTokenService local = AWSSecurityTokenServiceClientBuilder.standard().withCredentials(new DefaultAWSCredentialsProviderChain()).build();
            this.credentialsProvider = new STSAssumeRoleSessionCredentialsProvider.Builder(Config.get(Config.Key.IAM_ROLE_ARN), IAM_SESSION_NAME).withStsClient(local).build();
        } else {
            this.credentialsProvider = new DefaultAWSCredentialsProviderChain();
        }
        this.ec2 = AmazonEC2ClientBuilder.standard().withCredentials(this.credentialsProvider).build();
        this.iam = AmazonIdentityManagementClientBuilder.standard().withCredentials(this.credentialsProvider).build();
        this.s3 = AmazonS3ClientBuilder.standard().withCredentials(this.credentialsProvider).build();
        this.sts = AWSSecurityTokenServiceClientBuilder.standard().withCredentials(this.credentialsProvider).build();
    }

    protected final User createUser(final String userName) throws JSchException {
        final JSch jsch = new JSch();
        final com.jcraft.jsch.KeyPair keyPair = com.jcraft.jsch.KeyPair.genKeyPair(jsch, com.jcraft.jsch.KeyPair.RSA, 2048);
        final ByteArrayOutputStream osPublicKey = new ByteArrayOutputStream();
        final ByteArrayOutputStream osPrivateKey = new ByteArrayOutputStream();
        keyPair.writePublicKey(osPublicKey, userName);
        keyPair.writePrivateKey(osPrivateKey);
        final byte[] sshPrivateKeyBlob = osPrivateKey.toByteArray();
        final String sshPublicKeyBody = osPublicKey.toString();
        this.iam.createUser(new CreateUserRequest().withUserName(userName));
        final UploadSSHPublicKeyResult res = this.iam.uploadSSHPublicKey(new UploadSSHPublicKeyRequest().withUserName(userName).withSSHPublicKeyBody(sshPublicKeyBody));
        return new User(userName, sshPrivateKeyBlob, res.getSSHPublicKey().getSSHPublicKeyId());
    }

    protected final void deleteUser(final Context context, final String userName) {
        if (Config.get(Config.Key.DELETION_POLICY).equals("delete")) {
            if (Config.get(Config.Key.FAILURE_POLICY).equals("retain") && context.hasFailure()) {
                System.out.println("Skip user deletion because of stack failure in context and FAILURE_POLICY := retain");
            } else {
                final ListSSHPublicKeysResult res = this.iam.listSSHPublicKeys(new ListSSHPublicKeysRequest().withUserName(userName));
                this.iam.deleteSSHPublicKey(new DeleteSSHPublicKeyRequest().withUserName(userName).withSSHPublicKeyId(res.getSSHPublicKeys().get(0).getSSHPublicKeyId()));
                this.iam.deleteUser(new DeleteUserRequest().withUserName(userName));
            }
        }
    }

    protected final KeyPair createKey(final String keyName) {
        final CreateKeyPairResult res = this.ec2.createKeyPair(new CreateKeyPairRequest().withKeyName(keyName));
        System.out.println("keypair[" + keyName + "] created: " + res.getKeyPair().getKeyMaterial());
        return res.getKeyPair();
    }

    protected final void deleteKey(final Context context, final String keyName) {
        if (Config.get(Config.Key.DELETION_POLICY).equals("delete")) {
            if (Config.get(Config.Key.FAILURE_POLICY).equals("retain") && context.hasFailure()) {
                System.out.println("Skip key deletion because of stack failure in context and FAILURE_POLICY := retain");
            } else {
                this.ec2.deleteKeyPair(new DeleteKeyPairRequest().withKeyName(keyName));
                System.out.println("keypair[" + keyName + "] deleted");
            }
        }
    }

    protected final void createBucket(final String name, final String policy) {
        this.s3.createBucket(new CreateBucketRequest(name, Region.fromValue(this.getRegion())));
        this.s3.setBucketPolicy(name, policy);
    }

    protected final void createObject(final String bucketName, final String key, final String body) {
        this.s3.putObject(bucketName, key, body);
    }

    protected final void createObject(final String bucketName, final String key, final InputStream content, final String contentType, final long contentLength) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(contentLength);
        this.s3.putObject(bucketName, key, content, metadata);
    }

    protected final boolean doesObjectExist(final String bucketName, final String key) {
        return this.s3.doesObjectExist(bucketName, key);
    }

    protected final List<Tag> getObjectTags(final String bucketName, final String key) {
        return this.s3.getObjectTagging(new GetObjectTaggingRequest(bucketName, key)).getTagSet();
    }

    protected final void deleteObject(final Context context, final String bucketName, final String key) {
        if (Config.get(Config.Key.DELETION_POLICY).equals("delete")) {
            if (Config.get(Config.Key.FAILURE_POLICY).equals("retain") && context.hasFailure()) {
                System.out.println("Skip object deletion because of stack failure in context and FAILURE_POLICY := retain");
            } else {
                this.s3.deleteObject(bucketName, key);
            }
        }
    }

    protected final void emptyBucket(final Context context, final String name) {
        if (Config.get(Config.Key.DELETION_POLICY).equals("delete")) {
            if (Config.get(Config.Key.FAILURE_POLICY).equals("retain") && context.hasFailure()) {
                System.out.println("Skip bucket empty because of stack failure in context and FAILURE_POLICY := retain");
            } else {
                ObjectListing objectListing = s3.listObjects(name);
                while (true) {
                    objectListing.getObjectSummaries().forEach((summary) -> s3.deleteObject(name, summary.getKey()));
                    if (objectListing.isTruncated()) {
                        objectListing = s3.listNextBatchOfObjects(objectListing);
                    } else {
                        break;
                    }
                }
                VersionListing versionListing = s3.listVersions(new ListVersionsRequest().withBucketName(name));
                while (true) {
                    versionListing.getVersionSummaries().forEach((vs) -> s3.deleteVersion(name, vs.getKey(), vs.getVersionId()));
                    if (versionListing.isTruncated()) {
                        versionListing = s3.listNextBatchOfVersions(versionListing);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    protected long countBucket(final String name) {
        long count = 0;
        ObjectListing objectListing = s3.listObjects(name);
        while (true) {
            count += objectListing.getObjectSummaries().size();
            if (objectListing.isTruncated()) {
                objectListing = s3.listNextBatchOfObjects(objectListing);
            } else {
                break;
            }
        }
        return count;
    }

    protected final void deleteBucket(final Context context, final String name) {
        if (Config.get(Config.Key.DELETION_POLICY).equals("delete")) {
            if (Config.get(Config.Key.FAILURE_POLICY).equals("retain") && context.hasFailure()) {
                System.out.println("Skip bucket deletion because of stack failure in context and FAILURE_POLICY := retain");
            } else {
                this.emptyBucket(context, name);
                this.s3.deleteBucket(new DeleteBucketRequest(name));
            }
        }
    }

    protected final Vpc getDefaultVPC() {
        final DescribeVpcsResult res = this.ec2.describeVpcs(new DescribeVpcsRequest().withFilters(new Filter().withName("isDefault").withValues("true")));
        return res.getVpcs().get(0);
    }

    protected final List<Subnet> getDefaultSubnets() {
        final DescribeSubnetsResult res = this.ec2.describeSubnets(new DescribeSubnetsRequest().withFilters(new Filter().withName("defaultForAz").withValues("true")));
        return res.getSubnets();
    }

    protected final SecurityGroup getDefaultSecurityGroup() {
        final Vpc vpc = this.getDefaultVPC();
        final DescribeSecurityGroupsResult res = this.ec2.describeSecurityGroups(new DescribeSecurityGroupsRequest().withFilters(
                new Filter().withName("vpc-id").withValues(vpc.getVpcId()),
                new Filter().withName("group-name").withValues("default")
        ));
        return res.getSecurityGroups().get(0);
    }

    protected final String getRegion() {
        return new DefaultAwsRegionProviderChain().getRegion();
    }

    protected final String getAccount() {
        return this.sts.getCallerIdentity(new GetCallerIdentityRequest()).getAccount();
    }

    protected final String getCallerIdentityArn() {
        return this.sts.getCallerIdentity(new GetCallerIdentityRequest()).getArn();
    }

    protected final String random8String() {
        final String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        final int beginIndex = (int) (Math.random() * (uuid.length() - 7));
        final int endIndex = beginIndex + 7;
        return "r" + uuid.substring(beginIndex, endIndex); // must begin [a-z]
    }

}
