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
console.log "Using javaConfig: #{conf.javaConfig}"


fs = require 'fs'
Lazy = require 'lazy'
path = require 'path'

clean = (targetDir)->

  fs.readdirSync(targetDir).forEach (file)->
    toDelete = "#{targetDir}/#{file}"
    console.log "deleting file #{toDelete}"  
    fs.unlinkSync toDelete
    
  console.log "deleting folder #{targetDir}"
  fs.rmdirSync targetDir

create = (targetDir)->
  console.log "creating folder #{targetDir}"
  fs.mkdirSync conf.dirs["target"] 
  
prepare = (targetDir)->
  console.log "preparing target dir: #{targetDir}"
  if fs.existsSync(targetDir) then clean targetDir
  create targetDir
  

getDependencies = (javaConfig)->
  console.log "Getting dependencies..."
  new Lazy(fs.createReadStream(javaConfig,'utf8'))
    .lines
    .map (line)->
      line.toString('utf8')
    .filter (line)->
      /CLASSPATH/.test line
    .map (line)->
      match = /\/(.*\.jar)<\/CLASSPATH>/gi.exec line
    .filter (match)->
      match?
    .map (match)->
      match[1]
    .map (jarName)->
      console.log "Found line #{jarName}"
      to = "#{conf.dirs['target']}/#{jarName}"
      from = "#{conf.dirs['jars']}/#{jarName}"
       
      {
        from: from
        jarName: jarName
        to: to
      }
    .forEach (elementToProcess)-> 
      console.log "Processing file #{JSON.stringify(elementToProcess)}"
      fs.writeFileSync elementToProcess.to, fs.readFileSync(elementToProcess.from)
      
createClassPath = ()->
  #TODO
    
prepare conf.dirs["target"]
getDependencies javaConfig for javaConfig in conf.javaConfigs



