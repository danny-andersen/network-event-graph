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
		long start = System.currentTimeMillis();
		if (ipaddr != null) {
			devices = deviceService.getDevicesByIpAddr(ipaddr);
			log.info(String.format("Retrieved %d devices for ipaddr: %s in %d ms", devices.length, ipaddr, System.currentTimeMillis() - start));
		} else {
			if (hostname == null) {
				hostname = "*";
			}
			devices = deviceService.getDevicesByHostname(hostname);
			log.info(String.format("Retrieved %d devices for hostname: %s in %d ms", devices.length, hostname, System.currentTimeMillis() - start));
		}
		
		return devices;
	}
	
	@GET
	@Path("/detail")
	public Device getDeviceDetail(@QueryParam("id") Long deviceId) {
		Device device = null;
		long start = System.currentTimeMillis();
		if (deviceId != null) {
			device = deviceService.getDevice(deviceId);
			if (device != null) {
				log.info(String.format("Found device for id: %s in %d ms", deviceId, System.currentTimeMillis() - start));
			} else {
				log.info("No device found for id: " + deviceId);
			}
		}
		return device;
	}
	
	@Path("/local")
	@GET
	public Device[] getLocalDevices() {
		long start = System.currentTimeMillis();
		Device[] devices = deviceService.getLocalOrRemoteDevices(true);
		log.info(String.format("Retrieved %d local devices in %d ms", devices.length, System.currentTimeMillis() - start));
		return devices;
	}	
	
	@Path("/remote")
	@GET
	public Device[] getRemoteDevices() {
		long start = System.currentTimeMillis();
		Device[] devices = deviceService.getLocalOrRemoteDevices(false);
		log.info(String.format("Retrieved %d remote devices in %d ms", devices.length, System.currentTimeMillis() - start));
		return devices;
	}
}
