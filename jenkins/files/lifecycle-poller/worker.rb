#!/usr/bin/env ruby

require 'net/http'
require 'aws-sdk'
require 'json'
require 'uri'
require 'yaml'
require 'syslog/logger'

$log = Syslog::Logger.new 'poller'
$conf = YAML::load_file(__dir__ + '/poller.conf')
Aws.config.update(region: $conf['region'])
$log.info 'poller started'

def takeAgentTemporarilyOffline(agent)
  if system("java -jar /var/cache/jenkins/war/WEB-INF/jenkins-cli.jar -s http://localhost:8080 -noKeyAuth offline-node #{agent} -m 'scale down' --username admin --password #{$conf['masterAdminPassword']}")
    $log.info "agent #{agent} is marked as offline"
    return true
  else
    $log.error "agent #{agent} could not be marked as offline"
    return false
  end
end

def deleteAgent(agent)
  if system("java -jar /var/cache/jenkins/war/WEB-INF/jenkins-cli.jar -s http://localhost:8080 -noKeyAuth delete-node #{agent} --username admin --password #{$conf['masterAdminPassword']}")
    $log.info "agent #{agent} is deleted"
    return true
  else
    $log.error "agent #{agent} could not be deleted"
    return false
  end
end

def isAgentIdle(agent)
  url = URI.parse("http://localhost:8080/computer/#{agent}/api/xml")
  req = Net::HTTP::Get.new(url.to_s)
  req.basic_auth('admin', $conf['masterAdminPassword'])
  res = Net::HTTP.start(url.host, url.port) {|http|
    http.request(req)
  }
  if res.code == '200'
    if res.body.include? '<idle>true</idle>'
      return true
    elsif res.body.include? '<idle>false</idle>'
      return false
    else
      $log.error "unexpected body: #{res.body}"
      return false
    end
  elsif res.code == '404'
    return true
  else
    $log.error "unexpected response code: #{res.code}"
    return false
  end
end
def awaitAgentIdle(agent)
  endTime = Time.now.to_i + $conf['maxWaitInSeconds']
  while Time.now.to_i < endTime do
    if isAgentIdle agent
      $log.info "agent #{agent} is idle"
      return true
    end
    sleep 5 # seconds
  end
  $log.error "agent #{agent} is not idle, but wait time elapsed"
  return false
end
def completeLifecycleAction(token, hook, asg)
  autoscaling = Aws::AutoScaling::Client.new()
  autoscaling.complete_lifecycle_action(
    lifecycle_hook_name: hook,
    auto_scaling_group_name: asg,
    lifecycle_action_token: token,
    lifecycle_action_result: 'CONTINUE'
  )
end
def pollSQS()
  poller = Aws::SQS::QueuePoller.new($conf['queueUrl'])
  poller.poll do |msg|
    body = JSON.parse(msg.body)
    $log.debug "message #{body}"
    if body['Event'] == 'autoscaling:TEST_NOTIFICATION'
      $log.info 'received test notification'
    else
      if body['LifecycleTransition'] == 'autoscaling:EC2_INSTANCE_TERMINATING'
        $log.info "lifecycle transition for agent #{body['EC2InstanceId']}"
        takeAgentTemporarilyOffline body['EC2InstanceId']
        awaitAgentIdle body['EC2InstanceId']
        deleteAgent body['EC2InstanceId']
        completeLifecycleAction body['LifecycleActionToken'], body['LifecycleHookName'], body['AutoScalingGroupName']
      else
        $log.error "received unsupported lifecycle transition: #{body['LifecycleTransition']}"
      end
    end
  end
end

pollSQS