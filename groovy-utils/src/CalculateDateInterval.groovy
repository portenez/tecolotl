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
				
				"${line}".findAll(datePattern).each{ foundDate -> 
					
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

//assume that the date-times are already sorted
//the come from a log file

foundDates.each{key, day ->
	
	def prev = null
	
	day.points.eachWithIndex {it, i ->
		
		if(day.diffs == null){
			day.diffs = []
		}
		
		if(prev != null){
			
			def diff = [:]
			diff.from = prev
			diff.to = it
			diff.delta = it.time - prev.time
			
			day.diffs << diff
			
			println "$i,${diff.from}, ${diff.to}, ${diff.delta}, ${(diff.delta)/(1000*60)}, ${diff.delta/(1000*60*60)}"
		}
		prev = it
	}
	
	day.duration = day.max.time - day.min.time
	
	println """
[$key], 
start: [${day.min}], 
end: [${day.max}],  
duration: ${day.duration/(1000*60*60)} hrs, 
point count: ${day.points.size},
"""
}



