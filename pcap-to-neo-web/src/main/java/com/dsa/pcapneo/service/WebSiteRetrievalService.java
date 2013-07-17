package com.dsa.pcapneo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.WebSite;
import com.dsa.pcapneo.graph.repositories.DeviceRepository;

@Component
public class WebSiteRetrievalService {
	@Autowired DeviceRepository repo;

	public WebSite[] getWebSitesVisitedByHostname(String hostname) {
		Iterable<WebSite> sites = repo.getAllWebSitesVisitedByDevice(hostname);
		Set<WebSite> webSites = new HashSet<WebSite>();
		for (WebSite site : sites) {
			webSites.add(site);
		}
		return webSites.toArray(new WebSite[webSites.size()]);
	}

	public WebSite[] getWebSitesVisitedByIpAddr(String ipAddr) {
		Iterable<WebSite> sites = repo.getAllWebSitesVisitedByIpAddr(ipAddr);
		Set<WebSite> webSites = new HashSet<WebSite>();
		for (WebSite site : sites) {
			webSites.add(site);
		}
		return webSites.toArray(new WebSite[webSites.size()]);
	}
}
