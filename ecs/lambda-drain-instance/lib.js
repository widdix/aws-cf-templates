'use strict';

// see README.md for deployment instructions

const AWS = require('aws-sdk');
const ecs = new AWS.ECS({apiVersion: '2014-11-13'});
const sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const asg = new AWS.AutoScaling({apiVersion: '2011-01-01'});

const cluster = process.env.CLUSTER;
const queueUrl = process.env.QUEUE_URL;
const drainingTimeout = process.env.DRAINING_TIMEOUT;

async function getContainerInstanceArn(ec2InstanceId) {
  console.log(`getContainerInstanceArn(${ec2InstanceId})`);
  const listResult = await ecs.listContainerInstances({cluster: cluster, filter: `ec2InstanceId == '${ec2InstanceId}'`}).promise();
  return listResult.containerInstanceArns[0];
}

async function drainInstance(containerInstanceArn) {
  console.log(`drainInstance(${containerInstanceArn})`);
  await ecs.updateContainerInstancesState({cluster: cluster, containerInstances: [containerInstanceArn], status: 'DRAINING'}).promise();
}

async function wait(containerInstanceArn, autoScalingGroupName, lifecycleHookName, lifecycleActionToken, terminateTime) {
  console.log(`wait(${containerInstanceArn}, ${autoScalingGroupName}, ${lifecycleHookName}, ${lifecycleActionToken}, ${terminateTime})`);
  let payload = {
    Service: 'DrainInstance',
    Event: 'custom:DRAIN_WAIT',
    ContainerInstanceArn: containerInstanceArn,
    AutoScalingGroupName: autoScalingGroupName,
    LifecycleHookName: lifecycleHookName,
    LifecycleActionToken: lifecycleActionToken,
    TerminateTime: terminateTime
  }
  await sqs.sendMessage({
      QueueUrl: queueUrl,
      DelaySeconds: 60,
      MessageBody: JSON.stringify(payload)
    }).promise();
}

async function deleteMessage(receiptHandle) {
  console.log(`deleteMessage(${receiptHandle})`);
  await sqs.deleteMessage({
      QueueUrl: queueUrl,
      ReceiptHandle: receiptHandle
    }).promise();
}

async function countTasks(containerInstanceArn) {
  console.log(`countTasks(${containerInstanceArn})`);
  const listResult = await ecs.listTasks({cluster: cluster, containerInstance: containerInstanceArn}).promise();
  return listResult.taskArns.length;
}

async function terminateInstance(autoScalingGroupName, lifecycleHookName, lifecycleActionToken) {
  console.log(`terminateInstance(${autoScalingGroupName}, ${lifecycleHookName}, ${lifecycleActionToken})`);
  asg.completeLifecycleAction({
      AutoScalingGroupName: autoScalingGroupName, 
      LifecycleHookName: lifecycleHookName,
      LifecycleActionToken: lifecycleActionToken,
      LifecycleActionResult: 'CONTINUE'
    }).promise();
}

async function hearbeat(autoScalingGroupName, lifecycleHookName, lifecycleActionToken) {
  console.log(`hearbeat(${autoScalingGroupName}, ${lifecycleHookName}, ${lifecycleActionToken})`);
  asg.recordLifecycleActionHeartbeat({
      AutoScalingGroupName: autoScalingGroupName, 
      LifecycleHookName: lifecycleHookName,
      LifecycleActionToken: lifecycleActionToken
    }).promise();
}

exports.handler = async function(event, context) {
  console.log(`Invoke: ${JSON.stringify(event)}`);

  const body = JSON.parse(event.Records[0].body); // batch size is 1
  const receiptHandle = event.Records[0].receiptHandle;

  if (body.Service === 'AWS Auto Scaling' && body.Event === 'autoscaling:TEST_NOTIFICATION') {
    console.log('Ingoring autoscaling:TEST_NOTIFICATION')
  } else if (body.Service === 'AWS Auto Scaling' && body.LifecycleTransition === 'autoscaling:EC2_INSTANCE_TERMINATING') {
    let lifecycleActionToken = body.LifecycleActionToken;
    let containerInstanceArn = await getContainerInstanceArn(body.EC2InstanceId);
    await drainInstance(containerInstanceArn);
    await wait(containerInstanceArn, body.AutoScalingGroupName, body.LifecycleHookName, body.LifecycleActionToken, body.Time);
  } else if (body.Service === 'DrainInstance' && body.Event === 'custom:DRAIN_WAIT') {
    let taskCount = await countTasks(body.ContainerInstanceArn);
    if (taskCount === 0) {
      await terminateInstance(body.AutoScalingGroupName, body.LifecycleHookName, body.LifecycleActionToken);
    } else {
      let actionDuration = Math.abs(new Date(body.TerminateTime).getTime() - new Date().getTime()) / 1000;
      if (actionDuration < drainingTimeout) {
        await hearbeat(body.AutoScalingGroupName, body.LifecycleHookName, body.LifecycleActionToken);
        await wait(body.ContainerInstanceArn, body.AutoScalingGroupName, body.LifecycleHookName, body.LifecycleActionToken, body.TerminateTime);
      } else {
        console.log('Timeout for instance termination reached.');
        await terminateInstance(body.AutoScalingGroupName, body.LifecycleHookName, body.LifecycleActionToken);
      }
    }
  } else {
    console.log('Ignoring invalid event ...');
  }
  await deleteMessage(receiptHandle);
};
