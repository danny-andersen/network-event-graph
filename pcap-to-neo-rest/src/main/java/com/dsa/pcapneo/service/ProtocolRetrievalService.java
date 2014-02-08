package com.dsa.pcapneo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.Protocol;
import com.dsa.pcapneo.domain.session.ProtocolUsage;
import com.dsa.pcapneo.graph.repositories.DeviceRepository;
import com.dsa.pcapneo.graph.repositories.PortRepository;
import com.dsa.pcapneo.graph.repositories.ProtocolRepository;

@Component
public class ProtocolRetrievalService {
	@Autowired Neo4jTemplate template;
	@Autowired ProtocolRepository repo; 
	@Autowired PortRepository portRepo;
	@Autowired DeviceRepository devRepo;

	/**
	 * Find the most used protocols, i.e. the ones with the most sessions 
	 * @return ProtocolUsage[] array of ProtocolUsage in descending order of sessions
	 */
	public ProtocolUsage[] getAllProtocolUsage() {
		Iterable<Map<String, Object>> ps = repo.findAllProtocolsByUsage();
		return convertIterableToProtocolUsage(ps);
	}
	
	/**
	 * Find the most used protocols with the given name, i.e. the ones with the most sessions 
	 * @param protocol Only return protocols that match this one
	 * @param startDate optional Date range
	 * @param endDate optional Date range
	 * @return ProtocolUsage[] array of ProtocolUsage in descending order of sessions
	 */
	public ProtocolUsage[] getProtocolUsage(String protocol, long startDate, long endDate) {
		Iterable<Map<String, Object>> ps = repo.findProtocolUsage(protocol, startDate, endDate);
		return convertIterableToProtocolUsage(ps);
	}
	
	/**
	 * Find the protocols that are seen on a particular port
	 * @param portNo Port number
	 * @return List of protocols, sorted by the most seen
	 */
	public ProtocolUsage[] getProtocolsUsedOnPort(int portNo, long startDate, long endDate) {
		Iterable<Port> ports = portRepo.findByPort(portNo);
		Iterable<Map<String, Object>> ps = repo.findProtocolUsageByPort(ports.iterator().next(), startDate, endDate);
		return convertIterableToProtocolUsage(ps);
	}

	/**
	 * Find the protocols used by a particular device
	 * @param deviceId Id of device
	 * @return List of protocols, sorted by the most seen
	 */
	public ProtocolUsage[] getProtocolsUsedByDevice(long deviceId, long startTime, long endTime) {
		Device dev = devRepo.findByDeviceId(deviceId);
		Iterable<Map<String, Object>> pu = repo.findProtocolUsageByDevice(dev, startTime, endTime);
		return convertIterableToProtocolUsage(pu);
	}

	private ProtocolUsage[] convertIterableToProtocolUsage(Iterable<Map<String, Object>> ps) {
		ProtocolUsage[] protocolUsage = null;
		if (ps != null) {
			List<ProtocolUsage> puList = new ArrayList<ProtocolUsage>();
			for (Map<String, Object> p : ps) {
				ProtocolUsage pu = new ProtocolUsage();
				pu.setProtocol(template.convert(p.get("proto"), Protocol.class));
				Object count;
				count = p.get("numSessions");
				if (count != null) {
					pu.setSessionCount(template.convert(count, Integer.class));
				}
				count = p.get("numDevices");
				if (count != null) {
					pu.setDeviceCount(template.convert(count, Integer.class));
				}
				puList.add(pu);
			}
			protocolUsage = puList.toArray(new ProtocolUsage[puList.size()]);
		}
		return protocolUsage;
	}
	
}
