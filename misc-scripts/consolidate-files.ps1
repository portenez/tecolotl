#2013.02.13 - Victor Garcia: https://github.com/portenez
$dst = "DELog output.txt"

if(Test-Path ($dst)){
  Remove-Item $dst
}

ls DELog* | Foreach {
  get-content $_ | out-file -encoding utf8 -Append $dst
}
