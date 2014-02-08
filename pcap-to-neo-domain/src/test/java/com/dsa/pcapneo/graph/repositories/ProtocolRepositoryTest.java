package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.DeviceType;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.Protocol;
import com.dsa.pcapneo.domain.graph.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class ProtocolRepositoryTest {
	@Autowired
	ProtocolRepository protoRepo;
	@Autowired
	Neo4jTemplate template;
	@Autowired
	SessionArtefactFactory factory;

	@Test
	@Transactional
	public void findAllProtocolUsage() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"laptop2")), template.save(new User("user1")));
		Device dev4 = new Device("test4", template.save(new DeviceType(
				"laptop3")), template.save(new User("user1")));
		template.save(device);
		template.save(dev2);
		template.save(dev3);
		template.save(dev4);
		Protocol protoIp = template.save(new Protocol("ip"));
		Protocol protoTcp = template.save(new Protocol("tcp"));
		Protocol protoUdp = template.save(new Protocol("udp"));

		IpSession ip = new IpSession(this.factory);
		Set<Protocol> protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setFromDevice(device);
		ip.setToDevice(dev2);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setProtocols(protos);
		template.save(ip);
		Iterable<Map<String, Object>> res = protoRepo.findAllProtocolsByUsage();
		List<Protocol> protocols = new ArrayList<Protocol>();
		List<Integer> sess = new ArrayList<Integer>();
		for (Map<String, Object> r : res) {
			Protocol p = template.convert(r.get("proto"), Protocol.class);
			protocols.add(p);
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(protocols.size(), is(3));
		assertThat(protocols.get(0).getName(), is(protoIp.getName()));
		assertThat(sess.get(0), is(3));
		assertThat(protocols.get(1).getName(), is(protoTcp.getName()));
		assertThat(sess.get(1), is(2));
		assertThat(protocols.get(2).getName(), is(protoUdp.getName()));
		assertThat(sess.get(2), is(1));
	}

	@Test
	@Transactional
	public void findProtocolUsage() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"laptop2")), template.save(new User("user1")));
		Device dev4 = new Device("test4", template.save(new DeviceType(
				"laptop3")), template.save(new User("user1")));
		template.save(device);
		template.save(dev2);
		template.save(dev3);
		template.save(dev4);
		Protocol protoIp = template.save(new Protocol("ip"));
		Protocol protoTcp = template.save(new Protocol("tcp"));
		Protocol protoUdp = template.save(new Protocol("udp"));

		IpSession ip = new IpSession(this.factory);
		Set<Protocol> protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setFromDevice(device);
		ip.setToDevice(dev2);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setProtocols(protos);
		template.save(ip);
		Iterable<Map<String, Object>> res = protoRepo.findProtocolUsage("tcp", 0, new Date().getTime());
		List<Protocol> protocols = new ArrayList<Protocol>();
		List<Integer> sess = new ArrayList<Integer>();
		for (Map<String, Object> r : res) {
			Protocol p = template.convert(r.get("proto"), Protocol.class);
			protocols.add(p);
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(protocols.size(), is(1));
		assertThat(protocols.get(0).getName(), is(protoTcp.getName()));
		assertThat(sess.get(0), is(2));
	}

	@Test
	@Transactional
	public void findProtocolUsageByPort() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"laptop2")), template.save(new User("user1")));
		Device dev4 = new Device("test4", template.save(new DeviceType(
				"laptop3")), template.save(new User("user1")));
		template.save(device);
		template.save(dev2);
		template.save(dev3);
		template.save(dev4);
		Protocol protoIp = template.save(new Protocol("ip"));
		Protocol protoTcp = template.save(new Protocol("tcp"));
		Protocol protoUdp = template.save(new Protocol("udp"));
		Port port = template.save(new Port(1000));
		Port port2 = template.save(new Port(2000));
		Port port3 = template.save(new Port(3000));
		Port port4 = template.save(new Port(900));

		IpSession ip = new IpSession(this.factory);
		Set<Protocol> protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setFromDevice(device);
		ip.setToDevice(dev2);
		ip.setDestPort(port);
		ip.setSrcPort(port2);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		ip.setDestPort(port2);
		ip.setSrcPort(port3);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		ip.setDestPort(port2);
		ip.setSrcPort(port4);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setProtocols(protos);
		template.save(ip);
		Iterable<Map<String, Object>> res = protoRepo.findProtocolUsageByPort(
				port2, 0, new Date().getTime());
		List<Protocol> protocols = new ArrayList<Protocol>();
		List<Integer> sess = new ArrayList<Integer>();
		for (Map<String, Object> r : res) {
			Protocol p = template.convert(r.get("proto"), Protocol.class);
			protocols.add(p);
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(protocols.size(), is(3));
		assertThat(protocols.get(0).getName(), is(protoIp.getName()));
		assertThat(sess.get(0), is(3));
		assertThat(protocols.get(1).getName(), is(protoTcp.getName()));
		assertThat(sess.get(1), is(2));
		assertThat(protocols.get(2).getName(), is(protoUdp.getName()));
		assertThat(sess.get(2), is(1));

		res = protoRepo.findProtocolUsageByPort(port4, 0, new Date().getTime());
		List<String> protoNames = new ArrayList<String>();
		sess = new ArrayList<Integer>();
		for (Map<String, Object> r : res) {
			Protocol p = template.convert(r.get("proto"), Protocol.class);
			protoNames.add(p.getName());
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(protoNames.size(), is(2));
		assertThat(protoNames, hasItems("ip", "udp"));
		assertThat(sess.get(0), is(1));
	}

	@Test
	@Transactional
	public void findProtocolUsageByDevice() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"laptop2")), template.save(new User("user1")));
		Device dev4 = new Device("test4", template.save(new DeviceType(
				"laptop3")), template.save(new User("user1")));
		template.save(device);
		template.save(dev2);
		template.save(dev3);
		template.save(dev4);
		Protocol protoIp = template.save(new Protocol("ip"));
		Protocol protoTcp = template.save(new Protocol("tcp"));
		Protocol protoUdp = template.save(new Protocol("udp"));
		Port port = template.save(new Port(1000));
		Port port2 = template.save(new Port(2000));
		Port port3 = template.save(new Port(3000));
		Port port4 = template.save(new Port(900));

		IpSession ip = new IpSession(this.factory);
		Set<Protocol> protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setFromDevice(device);
		ip.setToDevice(dev2);
		ip.setDestPort(port);
		ip.setSrcPort(port2);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		ip.setDestPort(port2);
		ip.setSrcPort(port3);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		ip.setDestPort(port2);
		ip.setSrcPort(port4);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setProtocols(protos);
		template.save(ip);
		Iterable<Map<String, Object>> res = protoRepo.findProtocolUsageByDevice(device, 0, new Date().getTime());
		List<String> protocols = new ArrayList<String>();
		List<Integer> sess = new ArrayList<Integer>();
		for (Map<String, Object> r : res) {
			Protocol p = template.convert(r.get("proto"), Protocol.class);
			protocols.add(p.getName());
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(protocols.size(), is(2));
		assertThat(protocols, hasItems("ip", "tcp"));
		assertThat(sess, hasItems(1, 1));
	}
}
