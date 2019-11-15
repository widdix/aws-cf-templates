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

    @Test
    public void testGSI() {
        final String stackName = "dynamodb-" + this.random8String();
        try {
            this.createStack(stackName,
                    "state/dynamodb.yaml",
                    new Parameter()
                        .withParameterKey("PartitionKeyName").withParameterValue("id")
                        .withParameterKey("SortKeyName").withParameterValue("timestamp")
                        .withParameterKey("Attribute1Name").withParameterValue("organisation")
                        .withParameterKey("Attribute2Name").withParameterValue("category")
                        .withParameterKey("Index1PartitionKeyName").withParameterValue("timestamp")
                        .withParameterKey("Index2PartitionKeyName").withParameterValue("organisation")
                        .withParameterKey("Index2SortKeyName").withParameterValue("timestamp")
            );
            // TODO how can we check if this stack works?
        } finally {
            this.deleteStack(stackName);
        }
    }

}
