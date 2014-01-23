package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.DeviceType;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.User;

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
		template.save(new Port(1000));
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

	@Test
	@Transactional
	public void findAllPorts() {
		List<Integer> res = Arrays.asList(900, 1000, 2000, 3000);
		template.save(new Port(900));
		template.save(new Port(1000));
		template.save(new Port(2000));
		template.save(new Port(3000));
		//Find all ports >1000
		Iterable<Port> ports = portRepository.findAll(new Sort(Direction.ASC, "port"));
		Assert.notNull(ports);
		List<Integer> ps = new ArrayList<Integer>();
		for (Port p : ports) {
			ps.add(p.getPort());
		}
		assertThat(ps.size(), is(4));
		assertThat(ps.get(0), is(900));
		assertThat(ps.get(3), is(3000));
		assertThat(ps, is(res));
	}

	@Test
	@Transactional
	public void findPortsByRange() {
		List<Integer> res = Arrays.asList(1000, 2000, 3000);
		Port port = template.save(new Port(900));
		port = template.save(new Port(1000));
		port = template.save(new Port(2000));
		port = template.save(new Port(3000));
		//Find all ports >1000
		Iterable<Port> ports = portRepository.findAllByRange("port", 1000, 32768);
		Assert.notNull(ports);
		List<Integer> ps = new ArrayList<Integer>();
		for (Port p : ports) {
			ps.add(p.getPort());
		}
		assertThat(ps.size(), is(3));
		assertThat(ps, is(res));
	}

	@Test
	@Transactional
	public void findAllPortUsage() {
		Port port = template.save(new Port(1000));
		Port port2 = template.save(new Port(2000));
		Port port3 = template.save(new Port(3000));
		Port port4 = template.save(new Port(900));

		IpSession ip = new IpSession(this.factory);
		ip.setDestPort(port);
		ip.setSrcPort(port2);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setDestPort(port);
		ip.setSrcPort(port3);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setDestPort(port);
		ip.setSrcPort(port3);
		template.save(ip);
		Iterable<Map<String,Object>> res = portRepository.findAllPortUsage();
		List<Port> ports = new ArrayList<Port>();
		List<Integer> sess = new ArrayList<Integer>();
		for (Map<String,Object> r : res) {
			Port p = template.convert(r.get("port"), Port.class);
			ports.add(p);
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(ports.size(), is(3));
		assertThat(ports.get(0).getPort(), is(port.getPort()));
		assertThat(sess.get(0), is(3));
		assertThat(ports.get(1).getPort(), is(port3.getPort()));
		assertThat(sess.get(1), is(2));
		assertThat(ports.get(2).getPort(), is(port2.getPort()));
		assertThat(sess.get(2), is(1));
	}

	@Test
	@Transactional
	public void findPortRangeUsage() {
		Port port = template.save(new Port(1000));
		Port port2 = template.save(new Port(2000));
		Port port3 = template.save(new Port(3000));
		Port port4 = template.save(new Port(900));

		IpSession ip = new IpSession(this.factory);
		ip.setDestPort(port);
		ip.setSrcPort(port2);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setDestPort(port);
		ip.setSrcPort(port3);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setDestPort(port);
		ip.setSrcPort(port3);
		template.save(ip);
		Iterable<Map<String,Object>> res = portRepository.findPortRangeUsage(0, 2000);
		List<Port> ports = new ArrayList<Port>();
		List<Integer> sess = new ArrayList<Integer>();
		for (Map<String,Object> r : res) {
			Port p = template.convert(r.get("port"), Port.class);
			ports.add(p);
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(ports.size(), is(2));
		assertThat(ports.get(0).getPort(), is(port.getPort()));
		assertThat(sess.get(0), is(3));
		assertThat(ports.get(1).getPort(), is(port2.getPort()));
		assertThat(sess.get(1), is(1));
	}
}
