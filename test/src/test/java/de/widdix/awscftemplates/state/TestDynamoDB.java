package de.widdix.awscftemplates.state;

import com.amazonaws.services.cloudformation.model.Parameter;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Test;

public class TestDynamoDB extends ACloudFormationTest {

    @Test
    public void test() {
        final String stackName = "dynamodb-" + this.random8String();
        try {
            this.createStack(stackName,
                    "state/dynamodb.yaml",
                    new Parameter().withParameterKey("PartitionKeyName").withParameterValue("id")
            );
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(stackName);
        }
    }

}
