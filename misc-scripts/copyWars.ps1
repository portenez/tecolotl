#2013.12.30 - Victor Garcia: https://github.com/portenez
$rootFolder = "E:\views\vgarcia_cloud_enterprise\Cloud_Enterprise\license-manager\license-manager-parent"
$pattern = "license-manager.+?"
$patternForWebapps = "license-manager-resource.war"
$patternForLifeRay = "license-manager.+?\.war"
gci -Path $rootFolder -recurse | 
	where {$_.Name -match $pattern -and !$_.PSIsContainer} |
	% {
		if($_.Name -match $patternForWebapps){
			echo "to webapps" $_.Name
		}
		if($_.Name -match $patternForLifeRay){
			echo "to-lifeRay" $_.Name 
		}
	}
	


