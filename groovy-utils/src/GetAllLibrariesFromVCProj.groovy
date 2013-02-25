import com.google.common.io.Files;



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

def srcFolder = new File("z:\\Aurora\\Code\\SwfLoader")
def finder = new LibFilesFinder(srcFolder)

println("absolutes paths: " + finder.findAbsoluteFileNames(true));
println("absolutes paths no root: " + finder.findAbsoluteFileNames(false));

def backupTarget = new File("z:\\LIB\\backup-${System.currentTimeMillis()}-${finder.filtersFile.name}")
backupTarget.mkdirs();

finder.findAbsoluteFileNames(true).each{
	File srcFile = new File(it)	
	File dstFile = new File(backupTarget, srcFile.name)
	println("copying from: [${srcFile.absolutePath}] to: [${dstFile}]")
	Files.copy(srcFile, dstFile)
}