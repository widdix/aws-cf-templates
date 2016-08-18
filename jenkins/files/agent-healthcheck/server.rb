#!/usr/bin/env ruby

require 'net/http'
require 'aws-sdk'
require 'yaml'
require 'webrick'
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
  resp2 = ec2.describe_instances(
    instance_ids: [masterInstanceId]
  )
  masterIP = resp2.reservations[0].instances[0].private_ip_address
  url = URI.parse("http://#{masterIP}:8080/computer/#{agent}/api/xml")
  req = Net::HTTP::Get.new(url.to_s)
  req.basic_auth('admin', $conf['masterAdminPassword'])
  res = Net::HTTP.start(url.host, url.port) {|http|
    http.request(req)
  }
  if res.code == '200'
    if res.body.include? '<offline>true</offline>'
      return false
    elsif res.body.include? '<offline>false</offline>'
      return true
    else
      $log.error "unexpected body: #{res.body}"
      return false
    end
  else
    $log.error "unexpected response code: #{res.code}"
    return false
  end
end

server = WEBrick::HTTPServer.new :Port => 8080
server.mount_proc '/' do |req, res|
  if isHealthy($instanceId)
    res.status = 200
    res.body = "Healthy"
  else
    res.status = 503
    res.body = "Unhealthy!"
  end
end
server.start
