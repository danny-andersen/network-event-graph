package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dsa.pcapneo.domain.graph.IpAddress;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class IpAddressRepositoryTest {

	@Autowired Neo4jTemplate template;
	@Autowired IpAddressRepository ipAddressRepository;
	
	@Transactional
	@Test
	public void findByAddress() {
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		template.save(new IpAddress(ip1));
		template.save(new IpAddress(ip2));
		
		//Find ips
		Iterable<IpAddress> res = ipAddressRepository.findByIpAddrLike("192.168.1.*");
		List<String> addrs = new ArrayList<String>();
		for (IpAddress addr : res) {
			addrs.add(addr.getIpAddr());
		}
		assertThat(addrs.size(), is(2));
		assertThat(addrs, hasItems("192.168.1.1", "192.168.1.2"));
	}

	@Transactional
	@Test
	public void findAllIpAddresses() {
		String ip1 = "192.168.1.1";
		String ip2 = "83.33.1.2";
		String ip3 = "10.168.1.3";
		String ip4 = "1.1.1.4";
		template.save(new IpAddress(ip1));
		template.save(new IpAddress(ip2));
		template.save(new IpAddress(ip3));
		template.save(new IpAddress(ip4));
		
		//Find ips
		Iterable<IpAddress> res = ipAddressRepository.findByIpAddrLike("*");
		List<String> addrs = new ArrayList<String>();
		for (IpAddress addr : res) {
			addrs.add(addr.getIpAddr());
		}
		assertThat(addrs.size(), is(4));
		assertThat(addrs, hasItems("192.168.1.1", "83.33.1.2", "10.168.1.3", "1.1.1.4"));
	}
}
