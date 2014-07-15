import java.text.SimpleDateFormat

/**
 * Down and dirty way to get the interval between 
 * dates
 * 
 * @author vgarcia
 *
 */
// TODO move this to be passed as a parameter
def srcFile = new File("E:\\to-delete\\evening.xml")

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
		dateContainer.distinctDiffs = new TreeSet()
		dateContainer.diffs = []
		
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
			
			if(dateContainer.lowerBound == null || dateTime.time < dateContainer.lowerBound.time){
				dateContainer.lowerBound = dateTime
			}
			if(dateContainer.upperBound == null ||  dateContainer.upperBound.time < dateTime.time){
				dateContainer.upperBound = dateTime
			}
			
		}
}


//assume that the date-times are already sorted
//they come from a log file

dateContainers.each{key, dateContainer ->
	
	def prev = null
	
	dateContainer.points.eachWithIndex {it, i ->
		
		if(prev != null){
			
			def diff = [:]
			diff.from = prev
			diff.to = it
			diff.delta = it.time - prev.time
			
			dateContainer.diffs << diff
			dateContainer.distinctDiffs << diff.delta
			
			
			println "$i,${diff.from}, ${diff.to}, ${diff.delta}, ${(diff.delta)/(1000*60)}, ${diff.delta/(1000*60*60)}"
		}
		prev = it
	}
	
	dateContainer.duration = dateContainer.upperBound.time - dateContainer.lowerBound.time
	
	println """
[$key], 
start: [${dateContainer.lowerBound}], 
end: [${dateContainer.upperBound}],  
duration: ${dateContainer.duration/(1000*60*60)} hrs, 
point count: ${dateContainer.points.size},
distinctMsDiffs: ${dateContainer.distinctDiffs}
"""
}

println "File process done!!"



