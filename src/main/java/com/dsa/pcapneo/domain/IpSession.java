package com.dsa.pcapneo.domain;

public class IpSession extends Session {
	private static final String DOT = ".";
	private static final String COLON = ":";
	private static final String TCP = "tcp";
	private static final String UDP = "udp";

	private String ipSrc;
	private String ipDest;
	private int length;
	private String transport;
	private String srcPort;
	private String destPort;
	
	public IpSession(PcapSummary pcap) {
		super(pcap);
		this.ipDest = pcap.getIpDest();
		this.ipSrc = pcap.getIpSrc();
		this.length = pcap.getLength();
		this.transport = getTransport();
		if (this.transport.compareTo(TCP) == 0) {
			this.srcPort = pcap.getTcpSrcPort();
			this.destPort = pcap.getTcpSrcPort();
		} else if (this.transport.compareTo(TCP) == 0) {
			this.srcPort = pcap.getUdpSrcPort();
			this.destPort = pcap.getUdpDestPort();
		}
	}

	@Override
	public void insertSelfIntoGraph() {
		
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

	public String getIpSrc() {
		return ipSrc;
	}

	public void setIpSrc(String ipSrc) {
		this.ipSrc = ipSrc;
	}

	public String getIpDest() {
		return ipDest;
	}

	public void setIpDest(String ipDest) {
		this.ipDest = ipDest;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort) {
		this.srcPort = srcPort;
	}

	public String getDestPort() {
		return destPort;
	}

	public void setDestPort(String destPort) {
		this.destPort = destPort;
	}

}
