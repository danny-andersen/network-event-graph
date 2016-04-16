FROM jetty:latest
MAINTAINER danny_andersen@yahoo.com
#Add network graph web to jetty container
COPY pcap-to-neo-web/target/pcap-to-neo-web/ /var/lib/jetty/webapps/pcap-to-neo-web 
#Create mount points for Neo4J database and import directory
RUN mkdir -p /home/danny/Neo4J/Pcap.db
RUN mkdir -p /home/danny/pcapIn/:/home/danny/pcapIn
#To run this image, map local volumes to the above 2 directories
#And map 8080 port to a local port
