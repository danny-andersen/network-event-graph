package com.dsa.pcapneo.service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dsa.pcapneo.graph.repositories.PortRepository;

@Component
public class PortRetrievalServiceTest {
	@Autowired PortRepository repo;

	@Test
	public void getMostUsedPorts() {
	}

}
