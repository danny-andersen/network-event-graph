package com.dsa.pcapneo.domain.graph;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class WebSite {
	@GraphId
	Long id;

	// Used in index lookup
	public static final String ADDRESS = "address";
	
	@Indexed
	private String address;

	// Uris hosted on this site
	@RelatedTo(type = "HOSTS", direction = Direction.OUTGOING)
	Set<WebPath> uris;

	@RelatedTo(type = "DEVICES", direction = Direction.BOTH)
	Set<Device> devices;

	@RelatedTo(type = "REFERER", direction = Direction.INCOMING)
	Set<WebSite> referers;

	public WebSite() {

	}

	public WebSite(String address) {
		this.address = address;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<WebPath> getUris() {
		return uris;
	}

	public void setUris(Set<WebPath> uris) {
		this.uris = uris;
	}

	public void addUri(WebPath uri) {
		if (this.uris == null) {
			this.uris = new HashSet<WebPath>();
		}
		this.uris.add(uri);
	}

	public Set<Device> getDevices() {
		return devices;
	}

	public void setDevices(Set<Device> devices) {
		this.devices = devices;
	}

	public void addDevice(Device dev) {
		if (dev == null) {
			return;
		}
		if (this.devices == null) {
			this.devices = new HashSet<Device>();
		}
		this.devices.add(dev);
	}

	public void addReferer(WebSite site) {
		if (site == null) {
			return;
		}
		if (this.referers == null) {
			this.referers = new HashSet<WebSite>();
		}
		this.referers.add(site);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		WebSite other = (WebSite) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
