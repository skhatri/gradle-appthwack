API_KEY=
API_KEY=
cmd=$1
case $cmd in
list)
curl -X GET -v --trace-time -u "$API_KEY:" https://appthwack.com/api/project/
;;

devices)
curl -X GET -u "$API_KEY:" https://appthwack.com/api/device
;;

new)
curl -X POST -u "$API_KEY:" https://appthwack.com/api/project/ -F "name=$2" -F "type=3"
;;

newpool)
curl -X POST -u "$API_KEY:" https://appthwack.com/api/devicepool -F "name=$2" -F "devices=$3"
;;

pools)
curl -X GET -u "$API_KEY:" https://appthwack.com/api/devicepool
;;

xpool)
DEL_URL="https://appthwack.com/api/devicepool/$2"
curl -X DELETE -u "$API_KEY:"  https://appthwack.com/api/devicepool/$2
;;

ipa)
curl -X POST --trace-time --progress-bar -u "$API_KEY:" -v https://appthwack.com/api/file -F "name=$2" -F "file=@$3" -F "save=true" -F "type=iosapp"
;;

tests)
curl -X POST --progress-bar -v -u "$API_KEY:" https://appthwack.com/api/file -F "name=$2" -F "file=@$3" -F "save=true" -F "type=calabash"
;;

files)
curl -X GET -u "$API_KEY:" https://appthwack.com/api/file
;;

run)
curl -X POST -u "$API_KEY:" https://appthwack.com/api/run -F "project=$2" -F "name=$3" -F "app=$4" -F "pool=$5" -F "calabash=$6"
;;

status)
curl -X GET -u "$API_KEY:" https://appthwack.com/api/run/$2/$3/status
;;

reports)
curl -X GET -u "$API_KEY:" https://appthwack.com/api/run/$2 -d "max_rows=10" -d "page=1"
;;

public)
curl -X PUT -u "$API_KEY:" https://appthwack.com/api/run/$2/$3?public=true
;;

delete)
curl -X DELETE -u "$API_KEY:" https://appthwack.com/api/file/$2
;;

*)
 echo pass cmd

esac;

echo .
