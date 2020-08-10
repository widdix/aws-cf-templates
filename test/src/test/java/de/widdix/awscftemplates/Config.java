package de.widdix.awscftemplates;

public final class Config {

    public enum Key {
        TEMPLATE_DIR("TEMPLATE_DIR"),
        BUCKET_NAME("BUCKET_NAME"),
        BUCKET_REGION("BUCKET_REGION"),
        IAM_ROLE_ARN("IAM_ROLE_ARN"),
        HOSTED_ZONE_ID("HOSTED_ZONE_ID"),
        DOMAIN_SUFFIX("DOMAIN_SUFFIX"),
        ACM_CERTIFICATE_ARN("ACM_CERTIFICATE_ARN"),
        CLOUDFRONT_ACM_CERTIFICATE_ARN("CLOUDFRONT_ACM_CERTIFICATE_ARN"),
        DELETION_POLICY("DELETION_POLICY", "delete"),
        FAILURE_POLICY("FAILURE_POLICY", "rollback");

        private final String name;
        private final String defaultValue;

        Key(String name, String defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        Key(String name) {
            this.name = name;
            this.defaultValue = null;
        }
    }

    public static String get(final Key key) {
        final String env = System.getenv(key.name);
        if (env == null) {
            if (key.defaultValue == null) {
                throw new RuntimeException("config not found: " + key.name);
            } else {
                return key.defaultValue;
            }
        } else {
            return env;
        }
    }

    public static boolean has(final Key key) {
        final String env = System.getenv(key.name);
        if (env == null) {
            if (key.defaultValue == null) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
