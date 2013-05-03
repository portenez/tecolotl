#2013.05.04 - backup checkouts
#Victor Garcia
#
#This script needs to be run from within a VIEW/VOB.
#I tested it on a snapshot view, never tried on a dynamic view.
#
#Run this script from the folder you want to backup. For now the 
#target for the backup is hardcoded
#
cleartool lsco -s -r -me -cview |
	Foreach {
		$oldName = $_
		#TODO pass in the backup target as an argument to the script
		$newName = $oldName -replace '^\.\\','e:\checkout-backup\'
		$oldFile = Get-Item $oldName 
		if(! $oldFile.PSIsContainer){
			#echo $newName
			XCopy $oldName $newName
		}
	}