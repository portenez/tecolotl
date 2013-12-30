#2013.12.30 - Victor Garcia: https://github.com/portenez
$rootFolder = "E:\views\vgarcia_cloud_enterprise\Cloud_Enterprise\license-manager\license-manager-parent"
$pattern = "license-manager.+?"
$patternForWebapps = "license-manager-resource.war"
$patternForLifeRay = "license-manager.+?\.war"
$lifeRayPath = "E:\liferay-portal-6.2.0-ce-ga1"
$tomcatFolder = gci -Path $lifeRayPath -recurse | where {$_.PSIsContainer -and $_.Name -match ".*?tomcat.*?"}

echo "Tomcat liferayFolder " $tomcatFolder.Name

gci -Path $rootFolder -recurse | 
	where {$_.Name -match $pattern -and !$_.PSIsContainer} |
	% {
		if($_.Name -match $patternForWebapps){
			echo "to webapps" $_.Name
			cpi $_.FullName "$($tomcatFolder.FullName)/webapps"
		}
		if($_.Name -match $patternForLifeRay){
			echo "to-lifeRay" $_.Name 
			cpi $_.FullName $lifeRayPath/deploy
		}
	}
	


