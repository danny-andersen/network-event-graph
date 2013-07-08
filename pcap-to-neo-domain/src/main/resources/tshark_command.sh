sudo tshark -T fields -E separator=, -e frame.time_epoch -e frame.protocols -e ip.addr -e ip.len -e ip.proto -e tcp.port  -e udp.port -f "not arp" -e http.url -e http.referer -e http.location


