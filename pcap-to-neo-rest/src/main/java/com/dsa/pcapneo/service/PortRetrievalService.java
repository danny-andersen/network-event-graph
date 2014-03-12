package com.dsa.pcapneo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.session.PortUsage;
import com.dsa.pcapneo.graph.repositories.DeviceRepository;
import com.dsa.pcapneo.graph.repositories.PortRepository;

@Component
public class PortRetrievalService {
	@Autowired Neo4jTemplate template;
	@Autowired PortRepository repo;
	@Autowired DeviceRepository devRepo;

	/**
	 * Find the most used ports, i.e. the ones with the most sessions 
	 * @return PortUsage[] array of PortUsage in descending order of sessions
	 */
	public PortUsage[] getMostUsedPorts() {
		Iterable<Map<String, Object>> ps = repo.findAllPortUsage();
		return convertIterableToPortUsage(ps);
	}
	
	/**
	 * Find the most used ports, i.e. the ones with the most sessions 
	 * @param minPort Only return ports greater than this
	 * @param startDate optional Date range
	 * @param endDate optional Date range
	 * @return PortUsage[] array of PortUsage in descending order of sessions
	 */
	public PortUsage[] getMostUsedPortsByRange(int minPort, int maxPort, long startDate, long endDate) {
		Iterable<Map<String, Object>> ps = repo.findPortRangeUsage(minPort, maxPort, startDate, endDate);
		return convertIterableToPortUsage(ps);
	}
	
	public PortUsage[] getPortUsageOfDevice(long deviceId, long startDate, long endDate) {
		Device dev = devRepo.findByDeviceId(deviceId);
		Iterable<Map<String, Object>> ps = repo.findPortUsageOfDevice(dev, startDate, endDate);
		return convertIterableToPortUsage(ps);
	}
	
	private PortUsage[] convertIterableToPortUsage(Iterable<Map<String, Object>> ps) {
		PortUsage[] portUsage = null;
		if (ps != null) {
			List<PortUsage> puList = new ArrayList<PortUsage>();
			for (Map<String, Object> p : ps) {
				PortUsage pu = new PortUsage();
				pu.setPort(template.convert(p.get("port"), Port.class));
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
			portUsage = puList.toArray(new PortUsage[puList.size()]);
		}
		return portUsage;
	}
	
	/**
	 * Find the ports that connect to the most devices
	 * @param minPort Find ports greater or equal to this
	 * @return List of ports, sorted by the most connected\
	 */
	public PortUsage[] getMostConnectedPorts(int minPort, int maxPort, long startDate, long endDate) {
		Iterable<Map<String, Object>> pd = repo.findPortRangeDeviceUsage(minPort, maxPort, startDate, endDate);
		return convertIterableToPortUsage(pd);
	}

	/**
	 * Find the ports that connect to the most devices
	 * @param minPort Find ports greater or equal to this
	 * @return List of ports, sorted by the most connected\
	 */
	public PortUsage[] getMostConnectedPorts() {
		Iterable<Map<String, Object>> pd = repo.findAllPortDeviceUsage();
		return convertIterableToPortUsage(pd);
	}
}
