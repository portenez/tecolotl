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

clean = ()->
  targetDir = conf.dirs["target"]
  console.log "cleaning target file..."
  if fs.existsSync(targetDir) 
    fs.readdirSync(targetDir).forEach (file)->
      toDelete = "#{targetDir}/#{file}"
      console.log "deleting file #{toDelete}"  
      fs.unlinkSync toDelete
    
    console.log "deleting folder #{targetDir}"
    fs.rmdirSync targetDir
  console.log "creating folder #{targetDir}"
  fs.mkdirSync conf.dirs["target"]
  

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
    .map (line)->
      line = line.replace /\${3rdParty}/g , conf.dirs["3rdParty"]
      line = line.replace /\${jars}/g, conf.dirs["jars"]
      {
        fullPath: line
        fileName: path.basename(line)
      }
    .forEach (line)-> 
      fs.writeFileSync "#{conf.dirs['target']}/#{line.fileName}", fs.readFileSync(line.fullPath)
      
createClassPath = ()->
  #TODO
    
clean()
getDependencies()



