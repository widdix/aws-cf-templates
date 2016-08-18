#!/usr/bin/env ruby

require 'aws-sdk'
require 'sinatra'
require 'syslog/logger'

$log = Syslog::Logger.new 'healthcheck'
$conf = YAML::load_file(__dir__ + '/healthcheck.conf')
Aws.config.update(region: $conf['region'])
$log.info 'server started'

$instanceId = `curl -s http://169.254.169.254/latest/meta-data/instance-id`

def isHealthy(agent)
  autoscaling = Aws::AutoScaling::Client.new()
  ec2 = Aws::EC2::Client.new()
  resp1 = autoscaling.describe_auto_scaling_groups(
    auto_scaling_group_names: [$conf['masterASG']]
  )
  masterInstanceId = resp1.auto_scaling_groups[0].instances[0].instance_id
  puts masterInstanceId
  resp2 = ec2.describe_instances(
    instance_ids: [masterInstanceId]
  )
  masterIP = resp2.reservations[0].instances[0].private_ip_address
  puts masterIP


  #"cat /root/agent.xml | java -jar jenkins-cli.jar -s http://$masterIP:8080 -noKeyAuth create-node $(curl -s http://169.254.169.254/latest/meta-data/instance-id) --username admin --password ", {"Ref": "MasterAdminPassword"}, "\n"  
  #if system("java -jar jenkins-cli.jar -s http://#{masterIP}:8080 -noKeyAuth create-node #{agent} --username admin --password #{$conf['masterAdminPassword']}")
  #  $log.info "agent #{agent} is marked as offline"
  #  return true
  #else
  #  $log.error "agent #{agent} could not be marked as offline"
  #  return false
  #end

  return true
end

set :port, 8080
get '/' do
  if isHealthy(instanceId)
    status 200
    "Healthy"
  else
    status 503
    "Unhealthy!"
  end
end
