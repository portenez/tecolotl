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
  

getDependencies = ()->
  console.log "Getting dependencies..."
  new Lazy(fs.createReadStream(conf.antFile,'utf8'))
    .lines
    .map (line)->
      line.toString('utf8')
    .filter (line)->
      /pathelement/.test line
    .map (line)->
      match = /"(.*)"/.exec line
      match = match[1] #get first group
    .map (pathelement)->
      pathelement = pathelement.replace /\${3rdParty}/g , conf.dirs["3rdParty"]
      pathelement = pathelement.replace /\${jars}/g, conf.dirs["jars"]
      jarName = path.basename(pathelement)
      target = "#{conf.dirs['target']}/#{jarName}"
      {
        fullPath: pathelement
        jarName: jarName,
        target: target
      }
    .forEach (elementToProcess)-> 
      console.log "Processing file #{JSON.stringify(elementToProcess)}"
      fs.writeFileSync elementToProcess.target, fs.readFileSync(elementToProcess.fullPath)
      
createClassPath = ()->
  #TODO
    
prepare conf.dirs["target"]
getDependencies()



