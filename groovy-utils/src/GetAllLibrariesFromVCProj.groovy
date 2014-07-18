@Grab(group='com.google.guava', module='guava', version='17.0')

import com.google.common.io.Files;
/**
 * Victor Garcia, Feb 25, 2013
 * 
 * This script can be used to analyze the filter file
 * of a VC 2010 project and back up the corresponding
 * lib files
 * 
 * 
 * !!! THIS IS ALWAYS WORK IN PROGRESS !!!
 * NO GUARANTEE
 * 
 */
class RelativePathHelper{
	
	def srcPath;
	
	RelativePathHelper setSrcPath(srcPath){
		this.srcPath = srcPath.tokenize("\\")
		return this;
	}
	
	String obtainAbsolutePath(relativePath, boolean includeRoot){
		
		def  pathTokens = relativePath.tokenize('\\')
		
		Stack<String> srcPathStack = new Stack<String>()
		srcPathStack.addAll(srcPath)
		def out = ""
		
		pathTokens.each{
			if(it == ".."){
				srcPathStack.pop();
			}else{
				out="${out}\\${it}"
			}
		}
		
		if(!includeRoot && srcPathStack.size() <= 1){
			return out
		}
		
		srcPathStack.each{it->
			def postfix = out.startsWith("\\")? out:"\\${out}";
			out = "${it}${postfix}"
		}
		return out
	}
	
	public String toString(){
		return String.format("relativePathHelper: {srcPath:%s}", Arrays.toString(srcPath))
	}
	
}

class LibFilesFinder{
	
	def filtersFile
	def srcFolder
	
	LibFilesFinder(File srcFolder){
		this.srcFolder = srcFolder
		
		
		
		def findVCProj = {it ->
			def out = null;
			it.eachFile{
				if(it.name ==~ /.+?\.vcxproj\.filters/ ){
					println("found: " + it)
					out = it
				}
			}
			return out
		}
		
		filtersFile = findVCProj(srcFolder)
		
		if(!filtersFile){
			println("no proj file found")
			throw Exception("no filters file was found")
		}
		println("using files:" + filtersFile.canonicalPath)
	}
	
	def findAbsoluteFileNames(boolean includeDrive){
		def Project = new XmlSlurper().parse(filtersFile)
		def allLibraries = Project.depthFirst().collect{it}.findAll{it.@Include ==~ /.*\.[lL][iI][bB]/ }.collect{it.@Include}
		def pathTokens = srcFolder.canonicalPath.tokenize('\\')
		def relativePathHelper = new RelativePathHelper().setSrcPath(srcFolder.canonicalPath)
		def absolutePaths = allLibraries.collect{relativePathHelper.obtainAbsolutePath(it.toString(), includeDrive)}
	}
}


def ok = args
def cli = new CliBuilder(usage:'GetAllLibrariesFromVCProj.groovy')
cli.s('source project folder', args: 1)
cli.d('dest backup folder for lib files', args: 1)
cli.z('zip file command', args:1 )
cli.a('archive file', args:1)
cli.i('info for backup and unzip folders', args:1)

def options = cli.parse(args)

println options.s
println options.d
println options.z
println options.a
println options.i

def srcFolder = new File(options.s)
def finder = new LibFilesFinder(srcFolder)

println("absolutes paths: " + finder.findAbsoluteFileNames(true));
println("absolutes paths no root: " + finder.findAbsoluteFileNames(false));

def infoForFileName = String.format('%1$tY%1$tm%1$td-%1$tH%1$tM%1$tS.%1$tL', new Date());
if(options.i){
	infoForFileName ="${options.i} - ${infoForFileName}"
}


def backupTarget = new File("${options.d}\\${infoForFileName}-backup-${finder.filtersFile.name}")
backupTarget.mkdirs();

def unzipTarget = new File("${options.d}\\${infoForFileName}-unzip-${finder.filtersFile.name}")
unzipTarget.mkdirs();

println "backup-target: ${backupTarget}"
println "unzip-target: ${unzipTarget}"

//copy all the found files to the backup location
finder.findAbsoluteFileNames(true).each{
	File srcFile = new File(it)	
	File dstFile = new File(backupTarget, srcFile.name)
	println("copying from: [${srcFile.absolutePath}] to: [${dstFile}]")
	Files.copy(srcFile, dstFile)
}
//extrac files
finder.findAbsoluteFileNames(false).each{
	File srcFile = new File(it)
	File dstFile = new File(unzipTarget, srcFile.name)
	def command = "\"${options.z}\" e \"${options.a}\" -o\"${unzipTarget.absolutePath}\" \"${it.substring(1)}\" "
	def proc = command.execute()
	proc.waitFor()
	def exitCode = proc.exitValue()
	println "Executed command: [${command}] with result: [${exitCode}]"
	if(exitCode!=0){
		println proc.err.text
		println proc.in.text	
	}
	
}



