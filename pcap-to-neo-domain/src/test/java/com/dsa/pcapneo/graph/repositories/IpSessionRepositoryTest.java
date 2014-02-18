package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Protocol;
import com.dsa.pcapneo.domain.graph.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class IpSessionRepositoryTest {

	@Autowired Neo4jTemplate template;
	@Autowired IpSessionRepository ipSessionRepository;
	@Autowired SessionArtefactFactory factory;
	
	@Transactional
	@Test
	public void getIpSessionsByIpAddr() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));

		IpSession ip = new IpSession(this.factory);
		ip.setIpSrc(addr1);
		ip.setIpDest(addr2);
		long start = new Date().getTime();
		ip.setStartTime(start);
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByIpAddr(ip1, 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems("192.168.1.1"));
	}

	@Transactional
	@Test
	public void getIpSessionsByIpAddrs() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));

		IpSession ip = new IpSession(this.factory);
		ip.setIpSrc(addr1);
		ip.setIpDest(addr2);
		long start = new Date().getTime();
		ip.setStartTime(start);
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByIpAddrs(ip1, ip2, 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems("192.168.1.1"));
	}

	@Transactional
	@Test
	public void getIpSessionsByIpAddrAndProto() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		String ip3 = "192.168.1.3";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));
		IpAddress addr3 = template.save(new IpAddress(ip3));
		Protocol protoIp = template.save(new Protocol("ip"));
		Protocol protoTcp = template.save(new Protocol("tcp"));
		Protocol protoUdp = template.save(new Protocol("udp"));

		IpSession ip = new IpSession(this.factory);
		Set<Protocol> protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setIpSrc(addr1);
		ip.setIpDest(addr2);
		template.save(ip);
		ip = new IpSession(this.factory);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setIpSrc(addr2);
		ip.setIpDest(addr3);
		template.save(ip);
		ip = new IpSession(this.factory);
		protos = new HashSet<Protocol>();
		ip.setIpSrc(addr2);
		ip.setIpDest(addr3);
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setProtocols(protos);
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByIpAddrAndProtocol(ip2, "udp", 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems(ip2));
	}

	@Transactional
	@Test
	public void getIpSessionsByIpAddrsAndProto() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		String ip3 = "192.168.1.3";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));
		IpAddress addr3 = template.save(new IpAddress(ip3));
		Protocol protoIp = template.save(new Protocol("ip"));
		Protocol protoTcp = template.save(new Protocol("tcp"));
		Protocol protoUdp = template.save(new Protocol("udp"));

		IpSession ip = new IpSession(this.factory);
		Set<Protocol> protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setIpSrc(addr1);
		ip.setIpDest(addr2);
		template.save(ip);
		ip = new IpSession(this.factory);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setIpSrc(addr2);
		ip.setIpDest(addr3);
		template.save(ip);
		ip = new IpSession(this.factory);
		protos = new HashSet<Protocol>();
		ip.setIpSrc(addr2);
		ip.setIpDest(addr3);
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setProtocols(protos);
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByIpAddrsAndProtocol(ip1, ip2, "tcp", 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems(ip1));
	}

	@Transactional
	@Test
	public void getIpSessionsByDeviceId() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));
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
		ip.setIpSrc(addr1);
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

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByDevice(device.getDeviceId(), 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems(ip1));
	}

	@Transactional
	@Test
	public void getIpSessionsByDeviceIdAndProtocol() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));
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
		ip.setIpSrc(addr1);
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
		ip.setIpSrc(addr2);
		ip.setProtocols(protos);
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByDeviceAndProtocol(dev2.getDeviceId(), "udp", 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems(ip2));
	}
	
	@Transactional
	@Test
	public void getIpSessionsByDeviceIds() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		String ip3 = "192.168.1.3";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));
		IpAddress addr3 = template.save(new IpAddress(ip3));
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
		ip.setIpSrc(addr1);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoTcp);
		ip.setProtocols(protos);
		ip.setIpSrc(addr3);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev3);
		ip.setFromDevice(dev2);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setProtocols(protos);
		ip.setIpSrc(addr2);
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByDeviceIds(dev2.getDeviceId(), dev3.getDeviceId(), 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems(ip2));
	}
	
	@Transactional
	@Test
	public void getIpSessionsByDeviceIdsAndProtocol() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress addr1 = template.save(new IpAddress(ip1));
		IpAddress addr2 = template.save(new IpAddress(ip2));
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
		ip.setIpSrc(addr1);
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
		ip.setToDevice(dev3);
		ip.setFromDevice(dev2);
		protos = new HashSet<Protocol>();
		protos.add(protoIp);
		protos.add(protoUdp);
		ip.setIpSrc(addr2);
		ip.setProtocols(protos);
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByDeviceIdsAndProtocol(dev2.getDeviceId(), dev3.getDeviceId(), "ud.*", 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems(ip2));
	}

}
