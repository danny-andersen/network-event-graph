package com.dsa.pcapneo.domain.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Session;
import com.dsa.pcapneo.graph.repositories.SessionArtefactFactory;

@Repository
public class SessionFactory {
	@Autowired protected SessionArtefactFactory factory;
	
	public Session createSession(PcapSummary pcap) {
		Session session = null;
		//Determine the most specific session type
		if ((pcap.getHttpUrl() != null && !pcap.getHttpUrl().isEmpty())
				|| (pcap.getHttpReferer() != null && !pcap.getHttpReferer().isEmpty())
				|| (pcap.getHttpLocation() != null && !pcap.getHttpLocation().isEmpty())) {
			//Its a Http session
			session = new HttpSession(factory, pcap);
		} else if (pcap.getIpSrc() != null) {
			//Plain ip session
			session = new IpSession(factory, pcap);
		} else {
			session = new Session(factory, pcap);
		}
		return session;
	}

	public SessionArtefactFactory getFactory() {
		return factory;
	}

	public void setFactory(SessionArtefactFactory factory) {
		this.factory = factory;
	}

}
