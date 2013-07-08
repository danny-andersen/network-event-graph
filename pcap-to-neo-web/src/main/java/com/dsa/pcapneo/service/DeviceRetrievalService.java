package com.dsa.pcapneo.service;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.graph.repositories.DeviceRepository;

@Component
public class DeviceRetrievalService {
	@Autowired DeviceRepository repo;
	
	public Device[] getDevicesByHostname(String hostname) {
		Device[] devices = null;
		return devices;
	}
	
	public Device[] getDevicesByIpAddr(String ipaddr) {
		Iterable<Device> devIter = repo.getDevicesUsingIpAddress(ipaddr);
		Set<Device> devSet = new TreeSet<Device>(new Comparator<Device>() {

			@Override
			public int compare(Device o1, Device o2) {
				if (o1.getHostName() != null && o2.getHostName() != null) {
					return o1.getHostName().compareTo(o2.getHostName());
				}
				return o1.getDeviceId().compareTo(o2.getDeviceId());
			}
			
		});
		for (Device dev : devIter) {
			devSet.add(dev);
		}
		return devSet.toArray(new Device[devSet.size()]);
	}

}
