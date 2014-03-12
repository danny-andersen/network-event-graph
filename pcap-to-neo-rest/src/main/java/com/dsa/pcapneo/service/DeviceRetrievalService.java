package com.dsa.pcapneo.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.Protocol;
import com.dsa.pcapneo.graph.repositories.DeviceRepository;
import com.dsa.pcapneo.graph.repositories.IpAddressRepository;
import com.dsa.pcapneo.graph.repositories.PortRepository;
import com.dsa.pcapneo.graph.repositories.ProtocolRepository;

@Component
public class DeviceRetrievalService {
	@Autowired Neo4jTemplate template;
	@Autowired DeviceRepository repo;
	@Autowired IpAddressRepository ipaddrRepo;
	@Autowired PortRepository portRepo;
	@Autowired ProtocolRepository protocolRepo;
	
	public Device[] getDevicesByHostname(String hostname) {
		Device[] devices = null;
		//TODO: Complete
		return devices;
	}
	
	/**
	 * Returns the local devices that pass a K core decomposition
	 * where K is at least the number of edges that each connected 
	 * device has to other devices (by IP sessions)
	 * 
	 * @param kCore The number of edges that each graph must have
	 * @param port Optional. Set to 0 if dont care, 
	 * otherwise filter all device edges by the given port
	 * @return all matching devices
	 */
	public Device[] getLocalDevicesByKCore(int kCore, int port) {
		Set<Device> devices = new HashSet<Device>();
		//TODO: Complete this
		//First get all local devices as start points
		Iterable<Device> local = repo.getLocalDevices();
		for (Device d : local) {
			//For each device, check edges from / to other devices
			Set<Device> connected = getConnectedDevices(d);
			if (connected.size() >= kCore) {
				//Check that connected devices are part of the sub-graph
				//in that they have >= kCore edges
			}
			devices.add(d);
		}
		
		return devices.toArray(new Device[devices.size()]);
	}

	private Set<Device> getConnectedDevices(Device d) {
		Iterable<Device> c = repo.getDevicesConnectedToDevice(d);
		Set<Device> connected = new HashSet<Device>();
		for (Device dc : c) {
			connected.add(dc);
		}
		return connected;
	}
	
	public Device[] getDevicesByIpAddr(String ipaddr) {
		Set<Device> devSet = getDeviceSet();
		//Find matching ipddrs
		Iterable<IpAddress> ipaddrs = ipaddrRepo.findByIpAddrLike(ipaddr);
		for (IpAddress addr : ipaddrs) {
			devSet = getDevicesByIpAddress(devSet, addr);
		}
		return devSet.toArray(new Device[devSet.size()]);
	}

	public Device[] getDevicesUsingProtocol(String protocol, long start, long end) {
		Set<Device> devSet = getDeviceSet();
		if (protocol != null) {
			//Find matching protos
			Iterable<Protocol> protos = protocolRepo.findProtocolByName(protocol);
			for (Protocol proto : protos) {
				Iterable<Map<String, Object>> results = repo.getDevicesConnectedViaProtocol(proto, start, end);
				if (results != null) {
					for (Map<String, Object> res: results) {
						Device dev = template.convert(res.get("device"), Device.class);
						devSet.add(dev);
					}
				}
			}
		}
		return devSet.toArray(new Device[devSet.size()]);
	}

	public Device[] getDevicesUsingPort(int portNo, long startDate, long endDate) {
		Set<Device> devSet = getDeviceSet();
		Iterable<Port> port = portRepo.findByPort(portNo);
		if (port != null && port.iterator().hasNext()) {
			Iterable<Map<String, Object>> devIter = repo.getDevicesUsingPort(port.iterator().next(), startDate, endDate);
			for (Map<String, Object> dev : devIter) {
				devSet.add(template.convert(dev.get("device"), Device.class));
			}
		}
		return devSet.toArray(new Device[devSet.size()]);
	}

	public Device[] getLocalOrRemoteDevices(boolean local) {
		Set<Device> devSet = getDeviceSet();
		Iterable<Device> devIter = null;
		if (local) {
			devIter = repo.getLocalDevices();
		} else {
			devIter = repo.getRemoteDevices();
		}
		for (Device dev : devIter) {
			devSet.add(dev);
		}
		return devSet.toArray(new Device[devSet.size()]);
	}

	public Device getDevice(Long id) {
		Device device = repo.findByDeviceId(id);
		//Retrieve the lazy loaded stuff
		template.fetch(device);
		template.fetch(device.getDeviceType());
		return device;
	}
	
	private Set<Device> getDevicesByIpAddress(Set<Device> devices, IpAddress ip) {
		if (ip != null) {
			Iterable<Device> devIter = repo.getDevicesUsingIpAddressNode(ip);
			if (devIter != null) {
				for (Device dev : devIter) {
					devices.add(dev);
				}
			}
		}
		return devices;
	}
	
	private Set<Device> getDeviceSet() {
		return new TreeSet<Device>(new Comparator<Device>() {

			@Override
			public int compare(Device o1, Device o2) {
				if (o1.getHostName() != null && o2.getHostName() != null) {
					return o1.getHostName().compareTo(o2.getHostName());
				}
				return o1.getDeviceId().compareTo(o2.getDeviceId());
			}
			
		});

	}
	 
}
