package com.dsa.pcapneo.graph.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.DeviceType;

public interface DeviceRepository extends GraphRepository<Device> {
	Iterable<Device> findByDeviceTypeName(String name);

}
