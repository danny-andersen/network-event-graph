package com.dsa.pcapneo.domain;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PcapSummary {
	private static final Log log = LogFactory.getLog(PcapSummary.class);
	private static final String COMMA = ",";
	private static final ConcurrentHashMap<String, String> ipCache = new ConcurrentHashMap<String, String>();
	private long dtoi;
	private String[] protocols;
	private String ipSrc;
	private String ipDest;
	private int length;
	private String tcpSrcPort;
	private String tcpDestPort;
	private String udpSrcPort;
	private String udpDestPort;
	private String httpUrl;
	private String httpReferer;
	private String httpLocation;

	private static final String DOT = ".";
	private static final String COLON = ":";


	//-e frame.time_epoch -e frame.protocols -e ip.addr -e ip.len -e tcp.port  -e udp.port 
	//-e http.url -e http.referer -e http.location
	
	public void parseCsvString(String val)
			throws ParseException {
		try {
			StringTokenizer tokenizer = new StringTokenizer(val.toString(),	COMMA);
			this.setDtoi(Long.parseLong(tokenizer.nextToken().split("\\.")[0]));
			this.setProtocols(tokenizer.nextToken().split(COLON));
			this.setIpSrc(tokenizer.nextToken());
			this.setIpDest(tokenizer.nextToken());
			this.setLength(Integer.parseInt(tokenizer.nextToken()));
			this.setTcpSrcPort(tokenizer.nextToken());
			this.setTcpDestPort(tokenizer.nextToken());
			this.setUdpSrcPort(tokenizer.nextToken());
			this.setUdpDestPort(tokenizer.nextToken());
			this.setHttpUrl(tokenizer.nextToken());
			this.setHttpReferer(tokenizer.nextToken());
			this.setHttpLocation(tokenizer.nextToken());
		} catch (Exception e) {
			log.error("Failed to parse pcap entry: " + val.toString(), e);
		}
	}

	public long getDtoi() {
		return dtoi;
	}

	public void setDtoi(long dtoi) {
		this.dtoi = dtoi;
	}

	public String getIpSrc() {
		return ipSrc;
	}

	public String getHostSrc() {
		return getHostname(ipSrc);
	}

	public void setIpSrc(String ipSrc) {
		this.ipSrc = ipSrc;
	}

	public String getIpDest() {
		return ipDest;
	}

	public String getHostDest() {
		return getHostname(ipDest);
	}

	public void setIpDest(String ipDest) {
		this.ipDest = ipDest;
	}

	public String[] getProtocols() {
		return protocols;
	}

	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	
	public String getTcpSrcPort() {
		return tcpSrcPort;
	}

	public void setTcpSrcPort(String tcpSrcPort) {
		this.tcpSrcPort = tcpSrcPort;
	}

	public String getTcpDestPort() {
		return tcpDestPort;
	}

	public void setTcpDestPort(String tcpDestPort) {
		this.tcpDestPort = tcpDestPort;
	}

	public String getUdpSrcPort() {
		return udpSrcPort;
	}

	public void setUdpSrcPort(String udpSrcPort) {
		this.udpSrcPort = udpSrcPort;
	}

	public String getUdpDestPort() {
		return udpDestPort;
	}

	public void setUdpDestPort(String udpDestPort) {
		this.udpDestPort = udpDestPort;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getHttpReferer() {
		return httpReferer;
	}

	public void setHttpReferer(String httpReferer) {
		this.httpReferer = httpReferer;
	}

	public String getHttpLocation() {
		return httpLocation;
	}

	public void setHttpLocation(String httpLocation) {
		this.httpLocation = httpLocation;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PcapSummary [dtoi=");
		builder.append(dtoi);
		builder.append(", ipSrc=");
		builder.append(ipSrc);
		builder.append(", ipDest=");
		builder.append(ipDest);
		builder.append(", protocols=");
		for (String proto : protocols) {
			builder.append(proto);
			builder.append(":");
		}
		builder.append(", length=");
		builder.append(length);
		builder.append("]");
		return builder.toString();
	}

	public static String getHostname(String ipaddr) {
		String hostname = ipCache.get(ipaddr);
		if (hostname == null) {
			hostname = ipaddr;
			InetAddress addr = null;
			try {
				addr = InetAddress.getByName(ipaddr);
			} catch (UnknownHostException e) {
				log.warn("ipaddress could not be resolved: " + ipaddr);
			}
			if (addr != null) {
				hostname = addr.getCanonicalHostName();
			}
			ipCache.put(ipaddr, hostname);
		}
		return hostname;
	}

	private byte[] getIpBytes(String ipaddr) throws NumberFormatException {
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

}
