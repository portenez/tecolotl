/**
 *Work in progress.. This script can be used to remove all the .svn
 *references from a folder structure recursively. Can be customized to do the same for any
 *generetic repeating file
 *
 * The file parameter is hardcoded
 * 
 */
// TODO get this from arguments
def folder = new File("E:\\MSTRJMeterCode")

purgeSvnInt = {it, delete -> 

    it.eachDir({dir->
        purgeSvnInt(dir, delete);
    });
    
    
    it.eachFile{
        if(it.name == ".svn" || delete){
            println "${it.canonicalPath} [canWrite: ${it.canWrite()}]deleting"
            if(it.isDirectory()){
                purgeSvnInt(it,true)
            }
            it.delete()
        }
    }
}


purgeSvn = {
    println "purge svn for ${it.canonicalPath}"
    if(!it.exists()){
        println "input folder doesn't exist"
        return
    }
    purgeSvnInt(it,false)
}



purgeSvn(folder)
println "finished"