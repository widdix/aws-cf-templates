package de.widdix.awscftemplates.ecs;

import com.amazonaws.services.cloudformation.model.Parameter;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClientBuilder;
import com.amazonaws.services.ecs.model.DescribeContainerInstancesRequest;
import com.amazonaws.services.ecs.model.DescribeContainerInstancesResult;
import com.amazonaws.services.ecs.model.ListContainerInstancesRequest;
import com.amazonaws.services.ecs.model.ListContainerInstancesResult;
import de.widdix.awscftemplates.ACloudFormationTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

public class TestECSCluster extends ACloudFormationTest {

    private final AmazonECS ecs = AmazonECSClientBuilder.standard().withCredentials(this.credentialsProvider).build();

    @Test
    public void default() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "ecs-cluster-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(stackName,
                            "ecs/cluster.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    final String cluster = this.getStackOutputValue(stackName, "Cluster");
                    final Callable<Boolean> callable = () -> {
                        final ListContainerInstancesResult res1 = this.ecs.listContainerInstances(new ListContainerInstancesRequest().withCluster(cluster));
                        final DescribeContainerInstancesResult res2 = this.ecs.describeContainerInstances(new DescribeContainerInstancesRequest().withCluster(cluster).withContainerInstances(res1.getContainerInstanceArns()));
                        // check container instances
                        if (res2.getContainerInstances().size() != 2) {
                            throw new RuntimeException("2 instances expected, but saw " + res2.getContainerInstances().size());
                        }
                        if (!res2.getContainerInstances().get(0).getStatus().equals("ACTIVE")) {
                            throw new RuntimeException("container 0 status expected ACTIVE, but saw " + res2.getContainerInstances().get(0).getStatus());
                        }
                        if (!res2.getContainerInstances().get(0).getAgentConnected()) {
                            throw new RuntimeException("container 0 agent expected connected, but saw " + res2.getContainerInstances().get(0).getAgentConnected());
                        }
                        if (!res2.getContainerInstances().get(1).getStatus().equals("ACTIVE")) {
                            throw new RuntimeException("container 1 status expected ACTIVE, but saw " + res2.getContainerInstances().get(1).getStatus());
                        }
                        if (!res2.getContainerInstances().get(1).getAgentConnected()) {
                            throw new RuntimeException("container 1 agent expected connected, but saw " + res2.getContainerInstances().get(1).getAgentConnected());
                        }
                        return true;
                    };
                    Assert.assertTrue(this.retry(callable));
                } finally {
                    this.deleteStack(stackName);
                }
            } finally {
                this.deleteStack(vpcStackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

    @Test
    public void costOptimized() {
        final String vpcStackName = "vpc-2azs-" + this.random8String();
        final String stackName = "ecs-cluster-" + this.random8String();
        final String classB = "10";
        final String keyName = "key-" + this.random8String();
        try {
            this.createKey(keyName);
            try {
                this.createStack(vpcStackName,
                        "vpc/vpc-2azs.yaml",
                        new Parameter().withParameterKey("ClassB").withParameterValue(classB)
                );
                try {
                    this.createStack(stackName,
                            "ecs/cluster-cost-optimized.yaml",
                            new Parameter().withParameterKey("ParentVPCStack").withParameterValue(vpcStackName),
                            new Parameter().withParameterKey("KeyName").withParameterValue(keyName)
                    );
                    final String cluster = this.getStackOutputValue(stackName, "Cluster");
                    final Callable<Boolean> callable = () -> {
                        final ListContainerInstancesResult res1 = this.ecs.listContainerInstances(new ListContainerInstancesRequest().withCluster(cluster));
                        final DescribeContainerInstancesResult res2 = this.ecs.describeContainerInstances(new DescribeContainerInstancesRequest().withCluster(cluster).withContainerInstances(res1.getContainerInstanceArns()));
                        // check container instances
                        if (res2.getContainerInstances().size() != 2) {
                            throw new RuntimeException("2 instances expected, but saw " + res2.getContainerInstances().size());
                        }
                        if (!res2.getContainerInstances().get(0).getStatus().equals("ACTIVE")) {
                            throw new RuntimeException("container 0 status expected ACTIVE, but saw " + res2.getContainerInstances().get(0).getStatus());
                        }
                        if (!res2.getContainerInstances().get(0).getAgentConnected()) {
                            throw new RuntimeException("container 0 agent expected connected, but saw " + res2.getContainerInstances().get(0).getAgentConnected());
                        }
                        if (!res2.getContainerInstances().get(1).getStatus().equals("ACTIVE")) {
                            throw new RuntimeException("container 1 status expected ACTIVE, but saw " + res2.getContainerInstances().get(1).getStatus());
                        }
                        if (!res2.getContainerInstances().get(1).getAgentConnected()) {
                            throw new RuntimeException("container 1 agent expected connected, but saw " + res2.getContainerInstances().get(1).getAgentConnected());
                        }
                        return true;
                    };
                    Assert.assertTrue(this.retry(callable));
                } finally {
                    this.deleteStack(stackName);
                }
            } finally {
                this.deleteStack(vpcStackName);
            }
        } finally {
            this.deleteKey(keyName);
        }
    }

}
