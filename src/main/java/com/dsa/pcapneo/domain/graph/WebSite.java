package com.dsa.pcapneo.domain.graph;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class WebSite {
	@GraphId
	long id;
	
	private String address;
	
	//Uris hosted on this site
	@RelatedTo(type="HOSTS", direction=Direction.OUTGOING)
	Set<WebResource> uris;

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

	public Set<WebResource> getUris() {
		return uris;
	}

	public void setUris(Set<WebResource> uris) {
		this.uris = uris;
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
