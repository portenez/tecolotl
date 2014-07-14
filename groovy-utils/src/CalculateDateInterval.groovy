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
def dayPattern = ~/\d{4}-\d{2}-\d{2}/

println "processing file: ${srcFile.canonicalPath}"

def foundDates = [:]

srcFile.eachLine{line ->
	
        def gline = "${line}"
				def allDates = gline.findAll(datePattern)
				
				allDates.each{ foundDate -> 
					
					def day = foundDate.find(dayPattern)
					
					if(foundDates[day]== null){
						foundDates[day] = [:]
						foundDates[day].points = []
					}
					
					def curDay = foundDates[day]
					def curTime = dateFormat.parse(foundDate)
					
					curDay.points << curTime
					
					if(curDay.min == null || curTime.time < curDay.min.time){
						curDay.min = curTime
					}
					if(curDay.max == null ||  curDay.max.time < curTime.time){
						curDay.max = curTime
					}
					
				}
}


foundDates.each{key, day ->
	
	def prev = null
	
	day.points.each {
		
		if(day.diffs == null){
			day.diffs = []
		}
		
		if(prev != null){
			day.diffs << it.time - prev.time
		}
		prev = it
	}
	
	println "$key ${day.min} ${day.max}"
}



