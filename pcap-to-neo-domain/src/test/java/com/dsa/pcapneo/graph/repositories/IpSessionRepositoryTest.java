package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.IpSession;

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
		ip.setStartTime(new Date().getTime());
		template.save(ip);

		Iterable<IpSession> ips = this.ipSessionRepository.getIpSessionsByIpAddr(ip1, 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems("192.168.1.1"));
	}

}
