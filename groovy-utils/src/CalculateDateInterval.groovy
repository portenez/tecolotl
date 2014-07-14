import java.text.SimpleDateFormat

/**
 * Down and dirty way to get the interval between 
 * dates
 * 
 * @author vgarcia
 *
 */

def srcFile = new File("E:\\to-delete\\status.xml")
def datePattern = ~/\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}/
def dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

println "processing file: ${srcFile.canonicalPath}"

def foundDates = []

srcFile.eachLine{line ->
	
//        println "${line}"
				
        def gline = "${line}"
				def allDates = gline.findAll(datePattern)
				
				allDates.each{ foundDate -> 
//					println foundDate;
					foundDates << dateFormat.parse(foundDate)
				}
}

println foundDates

def diffs = []
def prev = null;

foundDates.each { 
	if(prev != null){
		diffs << it.time - prev.time 
	}
	prev = it;
}

println diffs
