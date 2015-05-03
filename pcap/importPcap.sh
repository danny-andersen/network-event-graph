dirs=$(ls 2013*)
for dir in ${dirs}
do
	find ${dir} -name "*.out" -exec java -jar pcap-to-neo-domain-1.0-SNAPSHOT.jar '{}' ';'
	
done
