#2013.05.04 - backup checkouts
cleartool lsco -s -r -me -cview |
	Foreach {
		$oldName = $_
		$newName = $oldName -replace '^\.\\','e:\checkout-backup\'
		$oldFile = Get-Item $oldName 
		if(! $oldFile.PSIsContainer){
			#echo $newName
			XCopy $oldName $newName
		}
	}