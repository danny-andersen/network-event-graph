package com.dsa.pcapneo.domain.graph;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.dsa.pcapneo.domain.session.PcapSummary;
import com.dsa.pcapneo.graph.repositories.SessionArtefactFactory;


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

	protected SessionArtefactFactory factory;
	
	public Session() {}
	
	public Session(SessionArtefactFactory factory) {
		this.factory = factory;
	}
	
	public Session(SessionArtefactFactory factory, PcapSummary pcap) {
		this.factory = factory;
		this.startTime = pcap.getDtoi();
		this.protocols = pcap.getProtocols();
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

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public SessionArtefactFactory getFactory() {
		return factory;
	}

	public void setFactory(SessionArtefactFactory factory) {
		this.factory = factory;
	}

	public long getStartTime() {
		return startTime;
	}

	
}
