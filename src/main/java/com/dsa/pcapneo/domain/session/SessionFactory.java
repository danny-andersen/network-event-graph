package com.dsa.pcapneo.domain.session;

import java.net.URI;
import java.net.URISyntaxException;

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
//			session.set
//				if (pcap.getHttpReferer() != null) {
//					this.referer = factory.getWebSite(pcap.getHttpReferer());
//				}
//				//Use URL if set otherwise use location
//				if (pcap.getHttpUrl() != null) {
//					parseUri(pcap.getHttpUrl());
//				} else if (pcap.getHttpLocation() != null) {
//					parseUri(pcap.getHttpLocation());
//				}
//				this.device = factory.getDeviceFromIpAddr(this.getIpSrc());
//			}
//		} else {
//			//Plain ip session
//			session = new IpSession();
//			session.init(pcap);
		}
//			public void setUri(String url) {
//				parseUri(url);
//			}
//			
//			private void parseUri(String location) {
//				if (location == null) {
//					return;
//				}
//				URI uri = null;
//				try {
//					uri = new URI(location);
//					this.webSite = factory.getWebSite(uri.getHost());
//					this.resource = factory.getWebPath(uri.getPath());
//				} catch (URISyntaxException e) {
//					log.error("Could not parse uri string: " + location, e);
//				}
//			}
//			
//			public void setUrl(String url) {
//				parseUri(url);
//			}
//			public void init(PcapSummary pcap) {
//				super.init(pcap);
//				try {
//					this.destIp = factory.getIpAddress(pcap.getIpDest());
//					this.srcIp = factory.getIpAddress(pcap.getIpSrc());
//					this.length = pcap.getLength();
//					this.transport = getTransport();
//					if (this.transport.compareTo(TCP) == 0) {
//						this.srcPort = factory.getPort(pcap.getTcpSrcPort());
//						this.destPort = factory.getPort(pcap.getTcpSrcPort());
//					} else if (this.transport.compareTo(UDP) == 0) {
//						this.srcPort = factory.getPort(pcap.getUdpSrcPort());
//						this.destPort = factory.getPort(pcap.getUdpDestPort());
//					}
//				} catch (Exception e) {
//					log.error("Failed to create IpSession from pcap: " + pcap.toString(), e);
//				}
//			}

			

		return session;
	}
	

}
