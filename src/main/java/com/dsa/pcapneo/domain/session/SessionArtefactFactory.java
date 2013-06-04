package com.dsa.pcapneo.domain.session;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.WebPath;
import com.dsa.pcapneo.domain.graph.WebSite;
import com.dsa.pcapneo.graph.repositories.DeviceRepository;

@Component
public class SessionArtefactFactory {
	private static final Log log = LogFactory.getLog(SessionArtefactFactory.class);

	@Autowired Neo4jTemplate template;
	@Autowired private DeviceRepository deviceRepo;

	public WebSite getWebSite(String site) {
		GraphRepository<WebSite> repo = template.repositoryFor(WebSite.class);
		WebSite website = repo.findByPropertyValue(WebSite.ADDRESS, site);
		if (website == null) {
			website = template.save(new WebSite(site));
		}
		return website;
	}

	public WebPath getWebPath(String path) {
		GraphRepository<WebPath> repo = template.repositoryFor(WebPath.class);
		WebPath webPath = repo.findByPropertyValue(WebPath.PATH, path);
		if (webPath == null) {
			webPath = template.save(new WebPath(path));
		}
		return webPath;
	}
	
	public Device getDeviceFromIpAddr(IpAddress ipaddr) {
		//Find device associated with this session
		Device device = null;
		Iterable<Device> devices = deviceRepo.getDevicesUsingIpAddress(ipaddr);
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
			//Create new device with unknown name
			device = new Device();
			device.setHostName("unknown_" + ipaddr);
			device.addIpAddr(ipaddr);
			device = template.save(device);
		}
		return device;
	}
	
	public IpAddress getIpAddress(String ip) {
		GraphRepository<IpAddress> repo = template.repositoryFor(IpAddress.class);
		IpAddress ipaddr = repo.findByPropertyValue(IpAddress.IPADDR, ip);
		if (ipaddr == null) {
			ipaddr = template.save(new IpAddress(ip));
		}
		return ipaddr;
	}

	public Port getPort(String portNumStr) {
		int portNum = -1;
		try {
			portNum = Integer.parseInt(portNumStr);
		} catch (NumberFormatException e) {
			log.warn("Failed to parse port number (ignoring): " + portNumStr);
		}
		Port port = null;
		if (portNum != -1) {
			GraphRepository<Port> repo = template.repositoryFor(Port.class);
			port = repo.findByPropertyValue(IpAddress.IPADDR, portNum);
			if (port == null) {
				port = template.save(new Port(portNum));
			}
		}
		return port;
	}
}
