package com.dsa.pcapneo.service;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.graph.repositories.DeviceRepository;
import com.dsa.pcapneo.graph.repositories.IpAddressRepository;

@Component
public class DeviceRetrievalService {
	@Autowired DeviceRepository repo;
	@Autowired IpAddressRepository ipaddrRepo;
	
	public Device[] getDevicesByHostname(String hostname) {
		Device[] devices = null;
		return devices;
	}
	
	public Device[] getDevicesByIpAddr(String ipaddr) {
		Set<Device> devSet = new TreeSet<Device>(new Comparator<Device>() {

			@Override
			public int compare(Device o1, Device o2) {
				if (o1.getHostName() != null && o2.getHostName() != null) {
					return o1.getHostName().compareTo(o2.getHostName());
				}
				return o1.getDeviceId().compareTo(o2.getDeviceId());
			}
			
		});
		//Find matching ipddrs
		Iterable<IpAddress> ipaddrs = ipaddrRepo.findByIpAddrLike(ipaddr);
		for (IpAddress addr : ipaddrs) {
			devSet = getDevicesByIpAddress(devSet, addr);
		}
		return devSet.toArray(new Device[devSet.size()]);
	}

	public Device[] getLocalOrRemoteDevices(boolean local) {
		Set<Device> devSet = new TreeSet<Device>(new Comparator<Device>() {

			@Override
			public int compare(Device o1, Device o2) {
				if (o1.getHostName() != null && o2.getHostName() != null) {
					return o1.getHostName().compareTo(o2.getHostName());
				}
				return o1.getDeviceId().compareTo(o2.getDeviceId());
			}
			
		});
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
	
}
