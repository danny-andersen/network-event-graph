mkdir -p /home/danny/Neo4J/Pcap.db
mkdir -p /home/danny/pcapIn/pcap
sudo docker run -d -p 8888:8080 -v /home/danny/Neo4J/Pcap.db:/home/danny/Neo4J/Pcap.db -v /home/danny/pcapIn/:/home/danny/pcapIn dannyandersen/network-event
#URL: http://localhost:8888/pcap-to-neo-web

