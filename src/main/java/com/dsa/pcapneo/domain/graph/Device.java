package com.dsa.pcapneo.domain.graph;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;


@NodeEntity
public class Device {
	@GraphId
	Long deviceId;

	@Indexed
	private String hostName;

	@RelatedTo(type = "IS_A")
	@Fetch private DeviceType deviceType;

	@RelatedTo(type = "USED_BY")
	@Fetch private User user;
	
	@RelatedTo(type = "CONNECTS_USING", direction = Direction.OUTGOING)
	@Fetch private Set<IpAddress> ipaddr;
	
	public Device() {
		
	}
	
	public Device(String name, DeviceType type) {
		this.hostName = name;
		this.deviceType = type;
	}

	public Device(String name, DeviceType type, User user) {
		this.hostName = name;
		this.deviceType = type;
		this.user = user;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType type) {
		this.deviceType = type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<IpAddress> getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(Set<IpAddress> ipaddr) {
		this.ipaddr = ipaddr;
	}
	
	public void addIpAddr(IpAddress ipaddr) {
		if (this.ipaddr == null) {
			this.ipaddr = new HashSet<IpAddress>();
		}
		this.ipaddr.add(ipaddr);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceId == null) ? 0 : deviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		return true;
	}

}
