package com.dsa.pcapneo.domain;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A session that represents a summary event captured by some network analyser, e.g. tshark
 * @author danny
 *
 */
public abstract class Session {
	private long dtoi;
	private String[] protocols;
	
	@Autowired
	GraphDatabaseService graphDb;
	
	public static Session createSession(PcapSummary pcap) {
		Session session = null;
		//Determine the most specific session type
		if (!pcap.getHttpUrl().isEmpty() || !pcap.getHttpReferer().isEmpty() || !pcap.getHttpLocation().isEmpty()) {
			//Its a Http session
			session = new HttpSession(pcap);
		} else {
			//Plain ip session
			session = new IpSession(pcap);
		}
		return session;
	}
	
	public Session(PcapSummary pcap) {
		this.dtoi = pcap.getDtoi();
		this.protocols = pcap.getProtocols();
	}
	
	public abstract void insertSelfIntoGraph();
	
	public String getTransport() {
		return null;
	}
	
	public long getDtoi() {
		return dtoi;
	}

	public void setDtoi(long dtoi) {
		this.dtoi = dtoi;
	}

	public String[] getProtocols() {
		return protocols;
	}

	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}

	
}
