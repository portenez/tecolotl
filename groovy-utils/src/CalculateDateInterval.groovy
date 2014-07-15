import java.text.SimpleDateFormat

/**
 * Down and dirty way to get the interval between 
 * dates
 * 
 * @author vgarcia
 *
 */
// TODO move this to be passed as a parameter
def srcFile = new File("E:\\to-delete\\status2.xml")


def dateTimePattern = ~/\d{4}-\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}/
def datePattern = ~/\d{4}-\d{2}-\d{2}/
def dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

println "processing file: ${srcFile.canonicalPath}"

def dateContainers = [:]

def extractDateFromDateTimeString = { dateTime ->
	return dateTime.find(datePattern)
}

def getOrNewDateContainer = { date ->
	
	def dateContainer = dateContainers[date]
	
	if(dateContainer== null){
		dateContainer = [:]
		dateContainer.points = []		
		dateContainers[date] = dateContainer
	}
	
	return dateContainer
}

srcFile.eachLine{line ->
				
		"${line}".findAll(dateTimePattern).each{ dateTime ->
			
			//first separate by day
			def dateContainer = 
					getOrNewDateContainer(
							extractDateFromDateTimeString(dateTime))
			 
			dateTime = dateFormat.parse(dateTime)
			
			dateContainer.points << dateTime
			
			if(dateContainer.min == null || dateTime.time < dateContainer.min.time){
				dateContainer.min = dateTime
			}
			if(dateContainer.max == null ||  dateContainer.max.time < dateTime.time){
				dateContainer.max = dateTime
			}
			
		}
}

//assume that the date-times are already sorted
//the come from a log file

dateContainers.each{key, dateContainer ->
	
	def prev = null
	
	dateContainer.points.eachWithIndex {it, i ->
		
		if(dateContainer.diffs == null){
			dateContainer.diffs = []
		}
		
		if(prev != null){
			
			def diff = [:]
			diff.from = prev
			diff.to = it
			diff.delta = it.time - prev.time
			
			dateContainer.diffs << diff
			
			println "$i,${diff.from}, ${diff.to}, ${diff.delta}, ${(diff.delta)/(1000*60)}, ${diff.delta/(1000*60*60)}"
		}
		prev = it
	}
	
	dateContainer.duration = dateContainer.max.time - dateContainer.min.time
	
	println """
[$key], 
start: [${dateContainer.min}], 
end: [${dateContainer.max}],  
duration: ${dateContainer.duration/(1000*60*60)} hrs, 
point count: ${dateContainer.points.size},
"""
}



