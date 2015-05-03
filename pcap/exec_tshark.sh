if [ $# -ne 2 ]
then
	echo "Usage $0: <duration in secs> <path + filename of output file>"
	exit 1
fi
sudo tshark -T fields -E separator=, -e frame.time_epoch -e frame.protocols -e ip.src -e ip.dst -e ip.len -e ip.proto -e tcp.port  -e udp.port -f "not arp and not port 53" -e http.url -e http.referer -e http.location -e ip.host -i eth0 -i wlan0 -a duration:$1 > $2
