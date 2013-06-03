package com.dsa.pcapneo.domain.graph;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class DeviceType {
	@GraphId
	Long deviceTypeId;

	String name;
	
	public DeviceType() {
		
	}
	
	public DeviceType(String type) {
		this.name = type;
	}
	
	
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceTypeId == null) ? 0 : deviceTypeId.hashCode());
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
		DeviceType other = (DeviceType) obj;
		if (deviceTypeId == null) {
			if (other.deviceTypeId != null)
				return false;
		} else if (!deviceTypeId.equals(other.deviceTypeId))
			return false;
		return true;
	}
	
	
}
