/**
 *Everything is hardcoded. This would need to change 
 */
// TODO this should come from arguments
def srcFile = new File("E:\\dump\\DELog 2012-08-12 03_58_03 UTC.TXT")
// TODO this should come from arguments as well
def targetFile = new File("E:\\dump\\target.txt")
def limitLines = 2
def noLimit = true

println "processing file [${srcFile.canonicalPath}]"
if(!srcFile.exists()){
    println "${srcFile.canonicalPath} doesn't exist"
    exit
}
println "Deleting target file [${targetFile.canonicalPath}]"
targetFile.delete()

// TODO how to make this generic??
def srcPattern = ~/\[(\d{4})-(\d{2})-(\d{2})\s+(\d{2}):(\d{2}):(\d{2}).(\d{3})\]/
def dstPattern = '[$2/$3/$1 $4:$5:$6.$7]'

srcFile.eachLine{line ->
    if(limitLines-- > 0 || noLimit){
//        println "${limitLines}: ${line}"
        def gline = "${line}"
        gline = gline.replaceAll(srcPattern,dstPattern)
        targetFile.append("${gline}\n")
    }
}

println "Done!"