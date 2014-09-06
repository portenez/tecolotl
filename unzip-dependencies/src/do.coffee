###
 * New coffeescript file
###
fs = require 'fs'
Promise = require 'promise'
readFile = Promise.denodeify fs.readFile

  


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


processAntFile = (file)->
  console.log file

readFile(conf.antFile,'utf8').then(processAntFile)
