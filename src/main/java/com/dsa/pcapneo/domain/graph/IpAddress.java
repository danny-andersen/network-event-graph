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
public class IpAddress {
	public static final String LOCAL_SUBNET = "192.168.1";
	public enum Location { LOCAL, REMOTE }

	@GraphId
	private Long id;
	
	public static final String IPADDR = "ipAddr";
	
	@Indexed
	@Fetch private String ipAddr;

	@Fetch private Location location;

	@RelatedTo(type="SERVER_PORT", direction=Direction.OUTGOING)
	private Set<Port> serverPorts;
	
	@RelatedTo(type="CLIENT_PORT", direction=Direction.OUTGOING)
	private Set<Port> clientPorts;

	public IpAddress() {
	}
	
	public IpAddress(String ipAddr) {
		this.ipAddr = ipAddr;
		//Determine if local or not
		if (ipAddr.startsWith(LOCAL_SUBNET)) {
			this.location = Location.LOCAL;
		} else {
			this.location = Location.REMOTE;
		}
	}

	public IpAddress(String ipAddr, Location location) {
		this.ipAddr = ipAddr;
		this.location = location;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		IpAddress other = (IpAddress) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Set<Port> getServerPort() {
		return serverPorts;
	}

	public void setServerPort(Set<Port> serverPort) {
		this.serverPorts = serverPort;
	}

	public void addServerPort(Port port) {
		if (port == null) {
			return;
		}
		if (this.serverPorts == null) {
			this.serverPorts = new HashSet<Port>();
		}
		this.serverPorts.add(port);
	}
	
	public Set<Port> getClientPort() {
		return clientPorts;
	}

	public void setClientPort(Set<Port> clientPort) {
		this.clientPorts = clientPort;
	}

	public void addClientPort(Port port) {
		if (port == null) {
			return;
		}
		if (this.clientPorts == null) {
			this.clientPorts = new HashSet<Port>();
		}
		this.clientPorts.add(port);
	}
	

	
}
