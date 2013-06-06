package com.dsa.pcapneo.domain.graph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.session.PcapSummary;
import com.dsa.pcapneo.domain.session.SessionArtefactFactory;


/**
 * A session that represents a summary event captured by some network analyser, e.g. tshark
 * @author danny
 *
 */

@NodeEntity
public abstract class Session {
	@GraphId
	private Long sessionId;
	
	private long startTime;

	private String[] protocols;

	public Session() {}
	
	public Session(PcapSummary pcap) {
		this.startTime = pcap.getDtoi();
		this.protocols = pcap.getProtocols();
	}
	
	public String getTransport() {
		return null;
	}
	
	public long getDtoi() {
		return startTime;
	}

	public void setStartTime(long dtoi) {
		this.startTime = dtoi;
	}

	public String[] getProtocols() {
		return protocols;
	}

	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}

	
}
