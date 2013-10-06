package com.dsa.pcapneo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.service.DeviceRetrievalService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Component
@Path("/device")
@Api(value="/device", description="Find and retrieve Devices")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {
	private static final Log log = LogFactory.getLog(DeviceResource.class);

	@Autowired
	DeviceRetrievalService deviceService;

	@GET
	@Path("/hostname/{hostname}")
	@ApiOperation(value="Find Devices by hostname search string", notes="Enter a hostname or a host search expression")
	public Device[] getDevicesByHostname(
			@ApiParam(value="hostname search string - if not set * is assumed", required = false)
			@PathParam("hostname") 
			String hostname) {
		Device[] devices = null;
		long start = System.currentTimeMillis();
		if (hostname == null) {
			hostname = "*";
		}
		devices = deviceService.getDevicesByHostname(hostname);
		log.info(String.format(
				"Retrieved %d devices for hostname: %s in %d ms",
				devices.length, hostname, System.currentTimeMillis() - start));
		return devices;
	}

	@GET
	@Path("/ipaddr/{ipaddr}")
	@ApiOperation(value="Find Devices by Ip Address search string", notes="Enter an Ipaddress or a ipaddr search expression, e.g. 192.*")
	public Device[] getDevices(
			@ApiParam(value="ipaddress search string - if not set * is assumed", required = false)
			@PathParam("ipaddr") 
			String ipaddr) {
		Device[] devices = null;
		long start = System.currentTimeMillis();
		if (ipaddr == null) {
			ipaddr = "*";
		}
		devices = deviceService.getDevicesByIpAddr(ipaddr);
		log.info(String.format("Retrieved %d devices for ipaddr: %s in %d ms",
				devices.length, ipaddr, System.currentTimeMillis() - start));

		return devices;
	}

	@GET
	@Path("/detail/{id}")
	@ApiOperation(value="Returns a single Device for the given ID", notes="Enter a valid device id")
	@ApiResponses(value = {
			@ApiResponse(code=404, message = "Device not found")
	})
	public Device getDeviceDetail(
			@ApiParam(value="The id of the device")
			@PathParam("id") Long deviceId) {
		Device device = null;
		long start = System.currentTimeMillis();
		if (deviceId != null) {
			device = deviceService.getDevice(deviceId);
			if (device != null) {
				log.info(String.format("Found device for id: %s in %d ms",
						deviceId, System.currentTimeMillis() - start));
			} else {
				log.info("No device found for id: " + deviceId);
			}
		}
		return device;
	}

	@Path("/local")
	@ApiOperation(value="Get all devices that have been flagged as local", notes="This is equivalent to looking for all hosts with an ipaddress of 192.168.*")
	@GET
	public Device[] getLocalDevices() {
		long start = System.currentTimeMillis();
		Device[] devices = deviceService.getLocalOrRemoteDevices(true);
		log.info(String.format("Retrieved %d local devices in %d ms",
				devices.length, System.currentTimeMillis() - start));
		return devices;
	}

	@Path("/remote")
	@ApiOperation(value="Get all devices that are remote, i.e. have been flagged as local", notes="This is equivalent to looking for all hosts that dont have an ipaddress of 192.168.*")
	@GET
	public Device[] getRemoteDevices() {
		long start = System.currentTimeMillis();
		Device[] devices = deviceService.getLocalOrRemoteDevices(false);
		log.info(String.format("Retrieved %d remote devices in %d ms",
				devices.length, System.currentTimeMillis() - start));
		return devices;
	}
}
