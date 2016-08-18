#!/usr/bin/env ruby

require 'daemons'

Daemons.run(__dir__ + '/server.rb', {:monitor => true})
