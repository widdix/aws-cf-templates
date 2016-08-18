#!/usr/bin/env ruby

require 'daemons'

Daemons.run(__dir__ + '/worker.rb', {:monitor => true})
