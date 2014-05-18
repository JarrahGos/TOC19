#/usr/bin/bash
## found in ~/.xinitrc
## runs the TOC program
sleep 5 ## wait for X to load
exec openbox & wmpid=$! ## start the window manager and save it's pid
sleep 5 ## wait for openbox to load
java -jar TOC19.jar ## run the TOC program
killall xterm ## remove the xterm windows in the main screen. 
## later options
# feh --bg-center image.jpg


## keep it all running. 
wait $wmpid
