package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.dsa.pcapneo.domain.graph.Port;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class PortRepositoryTest {
	@Autowired
	PortRepository portRepository;
	@Autowired
	Neo4jTemplate template;
	@Autowired
	SessionArtefactFactory factory;

	@Test
	@Transactional
	public void findPortbyPortNumber() {
		List<Integer> res = Arrays.asList(1000);
		Port port = template.save(new Port(1000));
		//Find port
		Iterable<Port> ports = portRepository.findByPort(1000);
		Assert.notNull(ports);
		List<Integer> ps = new ArrayList<Integer>();
		for (Port p : ports) {
			ps.add(p.getPort());
		}
		assertThat(ps.size(), is(1));
		assertThat(ps, is(res));
	}


}
