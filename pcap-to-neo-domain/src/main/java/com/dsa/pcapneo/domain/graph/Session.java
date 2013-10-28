package com.dsa.pcapneo.domain.graph;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.dsa.pcapneo.domain.session.PcapSummary;
import com.dsa.pcapneo.graph.repositories.SessionArtefactFactory;


/**
 * A session that represents a summary event captured by some network analyser, e.g. tshark
 * @author danny
 *
 */

@NodeEntity
public class Session {
	@GraphId
	private Long sessionId;
	
	@Indexed
	private long startTime;

	@RelatedTo(type="VIA_PROTOCOL", direction=Direction.OUTGOING)
	@Fetch protected Set<Protocol> protocols;

	protected SessionArtefactFactory factory;
	
	public Session() {}
	
	public Session(SessionArtefactFactory factory) {
		this.factory = factory;
	}
	
	public Session(SessionArtefactFactory factory, PcapSummary pcap) {
		this.factory = factory;
		this.startTime = pcap.getDtoi();
		this.protocols = factory.getProtocols(pcap.getProtocols());
	}
	
	public long getDtoi() {
		return startTime;
	}

	public void setStartTime(long dtoi) {
		this.startTime = dtoi;
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

	public Set<Protocol> getProtocols() {
		return protocols;
	}

	public void setProtocols(Set<Protocol> protocols) {
		this.protocols = protocols;
	}

	
}
