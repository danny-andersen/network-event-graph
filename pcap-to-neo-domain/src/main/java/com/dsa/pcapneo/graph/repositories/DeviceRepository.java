package com.dsa.pcapneo.graph.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.DeviceType;
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.User;
import com.dsa.pcapneo.domain.graph.WebSite;

public interface DeviceRepository extends GraphRepository<Device> {
	Device findByDeviceId(Long deviceId);
	
	Iterable<Device> findByDeviceTypeName(String name);

	@Query ("START device=node:Device(hostName={0}) MATCH device-[:IS_A]->dt return dt")
	DeviceType getDeviceType(String hostName);

	@Query ("START device=node:Device(hostName={0}) MATCH device-[:USED_BY]->user return user")
	Iterable<User> getUsersOfDevice(String hostName);
	
	@Query ("START device=node:Device(hostName={0}) " + 
			"MATCH device<-[:CONNECTS_FROM_DEVICE]-httpsession-[:CONNECTS_TO]->site " +
			"RETURN distinct site")
	Iterable<WebSite> getAllWebSitesVisitedByDevice(String hostName);
	
	@Query ("START ip=node:IpAddress(ipAddr={0}) " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"<-[:CONNECTS_FROM_DEVICE]-httpsession-[:CONNECTS_TO]->site " +
			"RETURN distinct site")
	Iterable<WebSite> getAllWebSitesVisitedByIpAddr(String ipAddr);

	@Query ("START ip=node({0}) " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	Iterable<Device> getDevicesUsingIpAddressNode(IpAddress ipaddr);

	@Query ("START ip=node:IpAddress(ipAddr={0}) " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	Iterable<Device> getDevicesUsingIpAddress(String ipaddr);

	@Query ("START ip=node:IpAddress(location='LOCAL') " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	Iterable<Device> getLocalDevices();

	@Query ("START ip=node:IpAddress(location='REMOTE') " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	Iterable<Device> getRemoteDevices();
	
	@Query ("START dev=node({0}) " +
			"MATCH dev-[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-s-" +
			"[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-d2 " +
			"RETURN DISTINCT d2")
	Iterable<Device> getDevicesConnectedToDevice(Device device); 
	
	@Query ("START port=node({0}) " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s-" +
			"[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-device " +
			"RETURN DISTINCT device, count(s) AS numSessions ORDER BY numSessions DESC")
	Iterable<Map<String, Object>> getDevicesConnectedToPort(Port port);
}
