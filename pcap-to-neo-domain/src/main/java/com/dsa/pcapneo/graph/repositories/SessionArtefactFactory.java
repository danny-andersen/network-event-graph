package com.dsa.pcapneo.graph.repositories;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.Protocol;
import com.dsa.pcapneo.domain.graph.WebPath;
import com.dsa.pcapneo.domain.graph.WebSite;

@Repository
public class SessionArtefactFactory {
	private static final Log log = LogFactory.getLog(SessionArtefactFactory.class);

	@Autowired Neo4jTemplate template;
	@Autowired private DeviceRepository deviceRepo;

	public SessionArtefactFactory sessionArtefactFactory() {
		return new SessionArtefactFactory();
	}
	
	public WebSite getWebSite(String site) {
		if (site == null) {
			return null;
		}
		GraphRepository<WebSite> repo = template.repositoryFor(WebSite.class);
		WebSite website = repo.findByPropertyValue(WebSite.ADDRESS, site);
		if (website == null) {
			website = template.save(new WebSite(site));
		}
		return website;
	}

	public WebPath getWebPath(String path) {
		if (path == null) {
			return null;
		}
		GraphRepository<WebPath> repo = template.repositoryFor(WebPath.class);
		WebPath webPath = repo.findByPropertyValue(WebPath.PATH, path);
		if (webPath == null) {
			webPath = template.save(new WebPath(path));
		}
		return webPath;
	}
	
	public Device getDeviceFromIpAddr(IpAddress ipaddr) {
		//Find device associated with this session
		if (ipaddr == null || ipaddr.getIpAddr().isEmpty()) {
			return null;
		}
		Device device = null;
		Iterable<Device> devices = deviceRepo.getDevicesUsingIpAddress(ipaddr.getIpAddr());
		if (devices != null) {
			Iterator<Device> iter = devices.iterator();
			if (iter != null && iter.hasNext()) {
				device = iter.next();
				if (iter.hasNext()) {
					log.warn("More than one device is using ip address: " + ipaddr.getIpAddr());
				}
			}
		}
		if (device == null) {
			device = new Device();
			//Look up device hostname
			try {
				//Create an inetaddress from ip string
				InetAddress addr = InetAddress.getByName(ipaddr.getIpAddr());
				//Get the ip bytes and recreate inetaddress
				InetAddress address = InetAddress.getByAddress(addr.getAddress());
				//Lookup and set hostname
				device.setHostName(address.getCanonicalHostName());
			} catch (UnknownHostException e) {
				device.setHostName(ipaddr.getIpAddr());
				log.info("Failed to lookup hostname for ip address: " + ipaddr.getIpAddr());
			}
			device.addIpAddr(ipaddr);
			device = template.save(device);
		}
		return device;
	}
	
	public IpAddress getIpAddress(String ip) {
		if (ip == null) {
			return null;
		}
		GraphRepository<IpAddress> repo = template.repositoryFor(IpAddress.class);
		IpAddress ipaddr = repo.findByPropertyValue(IpAddress.IPADDR, ip);
		if (ipaddr == null) {
			ipaddr = template.save(new IpAddress(ip));
		}
		return ipaddr;
	}

	public Port getPort(String portNumStr) {
		if (portNumStr == null) {
			return null;
		}
		int portNum = -1;
		try {
			portNum = Integer.parseInt(portNumStr);
		} catch (NumberFormatException e) {
			log.warn("Failed to parse port number (ignoring): " + portNumStr);
		}
		Port port = null;
		if (portNum != -1) {
			GraphRepository<Port> repo = template.repositoryFor(Port.class);
			port = repo.findByPropertyValue(Port.PORT, portNum);
			if (port == null) {
				port = template.save(new Port(portNum));
			}
		}
		return port;
	}

	public Set<Protocol> getProtocols(String[] protocols) {
		if (protocols == null) {
			return null;
		}
		Set<Protocol> protos = new HashSet<Protocol>();
		for (String proto : protocols) {
			if (proto != null) {
				GraphRepository<Protocol> repo = template.repositoryFor(Protocol.class);
				Protocol p = repo.findByPropertyValue(Protocol.NAME, proto);
				if (p == null) {
					p = template.save(new Protocol(proto));
				}
				protos.add(p);
			}
		}
		return protos;
	}
}
