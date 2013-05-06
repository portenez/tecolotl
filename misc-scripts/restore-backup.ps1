$targetDrive="z:"
$targetVob="Flex"
$backupLocation="e:\checkout-backup\"
$comment="my comment"

$targetDrive
cd "${$targetDrive}\${$targetVob}"
gci "${$backupLocation}\" -recurse | 
where {!$_.PSIsContainer}  | 
% {
	$old = $_.FullName
	$new = $old -replace 'e:\\checkout-backup\\',''
	echo $new
	cleartool co -c "${$comment}" $new
	XCopy $old $new
}