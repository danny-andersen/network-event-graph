package com.dsa.pcapneo.domain.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Session;

@Repository
public class SessionFactory {
	@Autowired protected SessionArtefactFactory factory;
	
	public static Session createSession(PcapSummary pcap) {
		Session session = null;
		//Determine the most specific session type
		if (!pcap.getHttpUrl().isEmpty() || !pcap.getHttpReferer().isEmpty() || !pcap.getHttpLocation().isEmpty()) {
			//Its a Http session
			session = new HttpSession();
			session.set
			session.init(pcap);
		} else {
			//Plain ip session
			session = new IpSession();
			session.init(pcap);
		}
		return session;
	}
	

}
