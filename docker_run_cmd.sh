mkdir -p /home/danny/Neo4J/Pcap.db
mkdir -p /home/danny/pcapIn/pcap
sudo docker run -d -p 8888:8080 -v $(pwd)/pcap-to-neo-web/target/pcap-to-neo-web:/var/lib/jetty/webapps/pcap-to-neo-web/ -v /home/danny/Neo4J/Pcap.db:/home/danny/Neo4J/Pcap.db -v /home/danny/pcapIn/:/home/danny/pcapIn jetty:latest

