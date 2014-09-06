###
 * New coffeescript file
###


argv = require 'yargs' 
  #TODO figure out why it has to be '--' change it to '-'
  .usage('Usage $0 --conf [file]')
  .demand(['conf'])
  .argv

#TODO change the following go be asynch
console.log "Loading configuration from file: #{argv.conf}"
conf = require argv.conf
console.log "Configuration loaded: #{JSON.stringify(conf)}"
console.log "Using antfile: #{conf.antFile}"


fs = require 'fs'
Lazy = require 'lazy'

new Lazy(fs.createReadStream(conf.antFile,'utf8'))
  .lines
  .map (line)->
    line.toString('utf8')
  .filter (line)->
    /pathelement/.test line
  .forEach (line)->
    console.log line
