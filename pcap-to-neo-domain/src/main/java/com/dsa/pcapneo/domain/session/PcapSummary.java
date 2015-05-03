package com.dsa.pcapneo.domain.session;

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
	private static final String TCP = "tcp";
	private static final String UDP = "udp";
	private static final ConcurrentHashMap<String, String> ipCache = new ConcurrentHashMap<String, String>();
	private long dtoi;
	private String[] protocols;
	private String ipSrc;
	private String ipDest;
	private int length;
	private int protocolNumber;
	private String srcPort;
	private String destPort;
	private String httpUrl;
	private String httpReferer;
	private String httpLocation;
	private String srcHost;
	private String destHost;
	

	// -e frame.time_epoch -e frame.protocols -e ip.addr -e ip.len -e tcp.port
	// -e udp.port
	// -e http.url -e http.referer -e http.location -e ip.host

	public PcapSummary() {
	}

	public PcapSummary(String pcapStr) throws Exception {
		parseCsvString(pcapStr);
	}

	public void parseCsvString(String val) throws Exception {
		// StringTokenizer tokenizer = new StringTokenizer(val.toString(),
		// COMMA);
		String[] parts = val.split(COMMA);
		int next = 3;
		String protocols = null;
		this.setDtoi(Long.parseLong(parts[0].split("\\.")[0]));
		if (parts.length > 1 && parts[1] != null && !parts[1].isEmpty()) {
			protocols = parts[1];
			this.setProtocols(protocols.split(":"));
		} else {
			//Empty session
			return;
		}
		int ipcnt = 0;
		for (String protocol : this.protocols) {
			if (protocol.contentEquals("ip")) {
				ipcnt++;
			}
		}
		if (ipcnt == 2) {
			//tunnelled -ignore for now?
			return;
//			next += 2;
		}
		if (parts.length > 2 && parts[2] != null && !parts[2].isEmpty()) {
			this.setIpSrc(parts[2]);
			if (parts.length > 3 && parts[3] != null && !parts[3].isEmpty()) {
				this.setIpDest(parts[3]);
				next = 4;
			} else {
				//broken session
				return;
			}
		}
		if (parts.length > next && parts[next] != null && !parts[next].isEmpty()) {
			this.setLength(Integer.parseInt(parts[next]));
		}
		next++;
		if (parts.length > next && parts[next] != null && !parts[next].isEmpty()) {
			this.setProtocolNumber(Integer.parseInt(parts[next]));
		}
		next++;
		if (parts.length > next && protocols.contains(TCP)) {
			this.setSrcPort(parts[next++]);
			this.setDestPort(parts[next++]);
		}
		if (parts.length > next && protocols.contains(UDP)) {
			next++;
			this.setSrcPort(parts[next++]);
			this.setDestPort(parts[next++]);
		}
		if (parts.length > next) {
			this.setHttpUrl(parts[next++]);
			if (parts.length > next) {
				this.setHttpReferer(parts[next++]);
				if (parts.length > next) {
					this.setHttpLocation(parts[next]);
				}
			}
		}
		if (parts[parts.length-1] != null) {
			this.setDestHost(parts[parts.length-1]);
			this.setSrcHost(parts[parts.length-2]);
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

	public int getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(int protocolNumber) {
		this.protocolNumber = protocolNumber;
	}

	public static ConcurrentHashMap<String, String> getIpcache() {
		return ipCache;
	}

	public String getSrcHost() {
		return srcHost;
	}

	public void setSrcHost(String srcHost) {
		this.srcHost = srcHost;
	}

	public String getDestHost() {
		return destHost;
	}

	public void setDestHost(String destHost) {
		this.destHost = destHost;
	}

}
