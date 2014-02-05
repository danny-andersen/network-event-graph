package com.dsa.pcapneo.graph.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.DeviceType;
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.Protocol;
import com.dsa.pcapneo.domain.graph.User;
import com.dsa.pcapneo.domain.graph.WebSite;

public interface DeviceRepository extends GraphRepository<Device> {
	public Device findByDeviceId(Long deviceId);
	
	public Iterable<Device> findByDeviceTypeName(String name);

	@Query ("START device=node:Device(hostName={0}) MATCH device-[:IS_A]->dt return dt")
	public DeviceType getDeviceType(String hostName);

	@Query ("START device=node:Device(hostName={0}) MATCH device-[:USED_BY]->user return user")
	public Iterable<User> getUsersOfDevice(String hostName);
	
	@Query ("START device=node:Device(hostName={0}) " + 
			"MATCH device<-[:CONNECTS_FROM_DEVICE]-httpsession-[:CONNECTS_TO]->site " +
			"RETURN distinct site")
	public Iterable<WebSite> getAllWebSitesVisitedByDevice(String hostName);
	
	@Query ("START ip=node:IpAddress(ipAddr={0}) " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"<-[:CONNECTS_FROM_DEVICE]-httpsession-[:CONNECTS_TO]->site " +
			"RETURN distinct site")
	public Iterable<WebSite> getAllWebSitesVisitedByIpAddr(String ipAddr);

	@Query ("START ip=node({0}) " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	public Iterable<Device> getDevicesUsingIpAddressNode(IpAddress ipaddr);

	@Query ("START ip=node:IpAddress(ipAddr={0}) " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	public Iterable<Device> getDevicesUsingIpAddress(String ipaddr);

	@Query ("START ip=node:IpAddress(location='LOCAL') " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	public Iterable<Device> getLocalDevices();

	@Query ("START ip=node:IpAddress(location='REMOTE') " +
			"MATCH ip<-[:CONNECTS_USING]-device " +
			"RETURN device")
	public Iterable<Device> getRemoteDevices();
	
	@Query ("START dev=node({0}) " +
			"MATCH dev-[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-s-" +
			"[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-d2 " +
			"RETURN DISTINCT d2")
	public Iterable<Device> getDevicesConnectedToDevice(Device device); 
	
	@Query ("START port=node({0}) " +
			"MATCH port-[:CONNECTS_FROM_PORT|CONNECTS_TO_PORT]-s-" +
			"[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-device " +
			"WHERE s.startTime >= {1} AND s.startTime <= {2} " +
			"RETURN DISTINCT device, count(s) AS numSessions ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> getDevicesUsingPort(Port port, long startTime, long endTime);

	@Query ("START proto=node({0}) " +
			"MATCH proto-[:VIA_PROTOCOL]-s-" +
			"[:CONNECTS_TO_DEVICE|CONNECTS_FROM_DEVICE]-device " +
			"WHERE s.startTime >= {1} AND s.startTime <= {2} " +
			"RETURN DISTINCT device, count(s) AS numSessions ORDER BY numSessions DESC")
	public Iterable<Map<String, Object>> getDevicesConnectedViaProtocol(Protocol proto, long startTime, long endTime);
}
