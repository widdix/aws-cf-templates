'use strict';

// see README.md for deployment instructions

const AWS = require('aws-sdk');
const ecs = new AWS.ECS({apiVersion: '2014-11-13'});
const sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const asg = new AWS.AutoScaling({apiVersion: '2011-01-01'});

const cluster = process.env.CLUSTER;
const QueueUrl = process.env.QUEUE_URL;
const drainingTimeout = process.env.DRAINING_TIMEOUT;

async function getContainerInstanceArn(ec2InstanceId) {
  const req = { cluster, filter: `ec2InstanceId == '${ec2InstanceId}'` };
  console.log("getContainerInstanceArn(", req, ")");
  const listResult = await ecs.listContainerInstances(req).promise();
  return listResult.containerInstanceArns[0];
}

async function countTasks(containerInstance) {
  const req = { cluster, containerInstance };
  console.log("countTasks(", req, ")");
  const listResult = await ecs.listTasks(req).promise();
  return listResult.taskArns.length;
}

function drainInstance(containerInstanceArn) {
  const req = { cluster, containerInstances: [containerInstanceArn], status: 'DRAINING' };
  console.log("drainInstance(", req, ")");
  return ecs.updateContainerInstancesState(req).promise();
}

function wait(res) {
  const payload = {
    Service: 'DrainInstance',
    Event: 'custom:DRAIN_WAIT',
    ContainerInstanceArn: res.ContainerInstanceArn,
    AutoScalingGroupName: res.AutoScalingGroupName,
    LifecycleHookName: res.LifecycleHookName,
    LifecycleActionToken: res.LifecycleActionToken,
    TerminateTime: res.TerminateTime,
  };
  console.log("wait(", payload, ")");
  return sqs.sendMessage({
    QueueUrl,
    DelaySeconds: 60,
    MessageBody: JSON.stringify(payload)
  }).promise();
}

function deleteMessage(ReceiptHandle) {
  const req = { QueueUrl, ReceiptHandle };
  console.log("deleteMessage(", req, ")");
  return sqs.deleteMessage(req).promise();
}

function terminateInstance(res) {
  const req = {
    AutoScalingGroupName: res.AutoScalingGroupName,
    LifecycleHookName: res.LifecycleHookName,
    LifecycleActionToken: res.LifecycleActionToken,
    LifecycleActionResult: 'CONTINUE'
  };
  console.log("terminateInstance(", req, ")");
  return asg.completeLifecycleAction(req).promise();
}

function heartbeat(res) {
  const req = {
    AutoScalingGroupName: res.AutoScalingGroupName,
    LifecycleHookName: res.LifecycleHookName,
    LifecycleActionToken: res.LifecycleActionToken,
  };
  console.log("heartbeat(", req, ")");
  return asg.recordLifecycleActionHeartbeat(req).promise();
}

exports.handler = async function (event, context) {
  console.log(`Invoke: ${JSON.stringify(event)}`);

  const body = JSON.parse(event.Records[0].body); // batch size is 1
  const receiptHandle = event.Records[0].receiptHandle;

  if (body.Service === 'AWS Auto Scaling' && body.Event === 'autoscaling:TEST_NOTIFICATION') {
    console.log('Ignoring autoscaling:TEST_NOTIFICATION')
  } else if (body.Service === 'AWS Auto Scaling' && body.LifecycleTransition === 'autoscaling:EC2_INSTANCE_TERMINATING') {
    body.ContainerInstanceArn = await getContainerInstanceArn(body.EC2InstanceId);
    body.TerminateTime = body.Time;
    await Promise.all([ drainInstance(body.ContainerInstanceArn), wait(body) ]);
  } else if (body.Service === 'DrainInstance' && body.Event === 'custom:DRAIN_WAIT') {
    const taskCount = await countTasks(body.ContainerInstanceArn);
    if (taskCount === 0) {
      await terminateInstance(body);
    } else {
      const actionDuration = Math.abs(new Date(body.TerminateTime).getTime() - new Date().getTime()) / 1000;
      if (actionDuration < drainingTimeout) {
        await Promise.all([ heartbeat(body), wait(body) ]);
      } else {
        console.log('Timeout for instance termination reached.');
        await terminateInstance(body);
      }
    }
  } else {
    console.log('Ignoring invalid event ...');
  }
  await deleteMessage(receiptHandle);
};
