package com.dsa.pcapneo.domain.graph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.dsa.pcapneo.domain.session.PcapSummary;
import com.dsa.pcapneo.graph.repositories.SessionArtefactFactory;

@NodeEntity
public class IpSession extends Session {
	private static final Log log = LogFactory.getLog(IpSession.class);

	private static final String DOT = ".";
	private static final String COLON = ":";

	@RelatedTo(type = "CONNECTS_FROM_IP")
	@Fetch private IpAddress srcIp;

	@RelatedTo(type = "CONNECTS_FROM_PORT")
	@Fetch private Port srcPort;

	@RelatedTo(type = "CONNECTS_TO_IP")
	@Fetch private IpAddress destIp;

	@RelatedTo(type = "CONNECTS_TO_PORT")
	@Fetch private Port destPort;

	private int length;
	private int protocolNumber;

	public IpSession() {
		super();
	}
	
	public IpSession(SessionArtefactFactory factory) {
		super(factory);
	}
	
	public IpSession(SessionArtefactFactory factory, PcapSummary pcap) {
		super(factory, pcap);
		if (pcap == null) {
			return;
		}
		try {
			this.destIp = factory.getIpAddress(pcap.getIpDest());
			this.srcIp = factory.getIpAddress(pcap.getIpSrc());
			this.length = pcap.getLength();
			this.srcPort = factory.getPort(pcap.getSrcPort());
			this.destPort = factory.getPort(pcap.getDestPort());
			this.protocolNumber = pcap.getProtocolNumber();
		} catch (Exception e) {
			log.error("Failed to create IpSession from pcap: " + pcap.toString(), e);
		}
	}

	public byte[] getIpBytes(String ipaddr) throws NumberFormatException {
		// Check delimiter
		byte[] retBytes = null;
		boolean isIpV4 = ipaddr.indexOf(DOT) > 0;
		if (isIpV4) {
			retBytes = new byte[4];
			int i = 0;
			for (String net : ipaddr.split(DOT)) {
				try {
					retBytes[i++] = Byte.parseByte(net);
				} catch (NumberFormatException e) {
					throw new NumberFormatException(ipaddr + ": " + net
							+ " is not a number");
				}
			}
		} else {
			retBytes = new byte[16];
			int i = 0;
			for (String net : ipaddr.split(COLON)) {
				short net2;
				try {
					net2 = Short.parseShort(net);
				} catch (NumberFormatException e) {
					throw new NumberFormatException(ipaddr + ": " + net
							+ " is not a number");
				}
				retBytes[i++] = (byte) ((net2 & 0xff00) >> 8);
				retBytes[i++] = (byte) (net2 & 0x00ff);
			}
		}
		return retBytes;
	}

	public IpAddress getIpSrc() {
		return srcIp;
	}

	public void setIpSrc(IpAddress ipSrc) {
		this.srcIp = ipSrc;
	}

	public IpAddress getIpDest() {
		return destIp;
	}

	public void setIpDest(IpAddress ipDest) {
		this.destIp = ipDest;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Port getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(Port srcPort) {
		this.srcPort = srcPort;
	}

	public Port getDestPort() {
		return destPort;
	}

	public void setDestPort(Port destPort) {
		this.destPort = destPort;
	}

	public IpAddress getSrcIp() {
		return srcIp;
	}

	public void setSrcIp(IpAddress srcIp) {
		this.srcIp = srcIp;
	}

	public IpAddress getDestIp() {
		return destIp;
	}

	public void setDestIp(IpAddress destIp) {
		this.destIp = destIp;
	}

	public int getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(int protocolNumber) {
		this.protocolNumber = protocolNumber;
	}


}
