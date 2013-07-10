package com.dsa.pcapneo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.service.DeviceRetrievalService;

@Component
@Path("/device")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {
	private static final Log log = LogFactory.getLog(DeviceResource.class);
	
	@Autowired DeviceRetrievalService deviceService;
	
	@GET
	public Device[] getDevices(@QueryParam("hostname") String hostname,
				@QueryParam("ipaddr") String ipaddr) {
		Device[] devices = null;
		if (ipaddr != null) {
			devices = deviceService.getDevicesByIpAddr(ipaddr);
			log.info(String.format("Retrieved %d devices for ipaddr: %s", devices.length, ipaddr));
		} else {
			if (hostname == null) {
				hostname = "*";
			}
			devices = deviceService.getDevicesByHostname(hostname);
			log.info(String.format("Retrieved %d devices for hostname: %s", devices.length, hostname));
		}
		//Null ports in device ipaddrs
		for (Device device : devices) {
			for (IpAddress ip : device.getIpaddr()) {
				ip.setClientPort(null);
				ip.setServerPort(null);
			}
		}
		
		return devices;
	}
	
	@Path("/local")
	@GET
	public Device[] getLocalDevices() {
		Device[] devices = deviceService.getLocalOrRemoteDevices(true);
		log.info(String.format("Retrieved %d local devices", devices.length));
		return devices;
	}	
	
	@Path("/remote")
	@GET
	public Device[] getRemoteDevices() {
		Device[] devices = deviceService.getLocalOrRemoteDevices(false);
		log.info(String.format("Retrieved %d remote devices", devices.length));
		return devices;
	}
}
