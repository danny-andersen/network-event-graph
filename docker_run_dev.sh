mkdir -p /home/danny/Neo4J/Pcap.db
mkdir -p /home/danny/pcapIn/pcap
#Note: This runs the dev version of the web site. Make sure the contents of pcap-to-neo-web/target/pcap-to-neo-web/WEB-INF is copied to yo/app/ otherwise the REST services wont be deployed...
sudo docker run -d -p 8888:8080 -v $(pwd)/pcap-to-neo-web/yo/app:/var/lib/jetty/webapps/pcap-to-neo-web/ -v /home/danny/Neo4J/Pcap.db:/home/danny/Neo4J/Pcap.db -v /home/danny/pcapIn/:/home/danny/pcapIn jetty:latest

