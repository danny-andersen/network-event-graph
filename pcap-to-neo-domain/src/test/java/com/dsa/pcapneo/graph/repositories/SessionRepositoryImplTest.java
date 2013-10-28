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
import com.dsa.pcapneo.domain.graph.SessionSummary;
import com.dsa.pcapneo.graph.repositories.SessionRepository.SessionQueryType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class SessionRepositoryImplTest {

	@Autowired
	Neo4jTemplate template;
	@Autowired
	SessionRepoImpl sessionRepository;
	@Autowired
	SessionArtefactFactory factory;

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

		List<IpSession> ips = this.sessionRepository.getIpSessionsByIpAddr(ip1, "", 0, new Date().getTime());
		List<String> src = new ArrayList<String>();
		for (IpSession s : ips) {
			src.add(s.getSrcIp().getIpAddr());
		}
		assertThat(src.size(), is(1));
		assertThat(src, hasItems("192.168.1.1"));
	}

	@Transactional
	@Test
	public void getIpSessionSummaryByIpAddr() {
		String srcIp = "192.168.1.1";
		String destIp1 = "192.168.1.2";
		String destIp2 = "192.168.1.3";
		String destIp3 = "192.168.1.4";
		String destIp4 = "192.168.1.5";
		IpAddress srcIpAddress = template.save(new IpAddress(srcIp));
		IpAddress[] addrs = new IpAddress[4];
		addrs[0] = template.save(new IpAddress(destIp1));
		addrs[1] = template.save(new IpAddress(destIp2));
		addrs[2] = template.save(new IpAddress(destIp3));
		addrs[3] = template.save(new IpAddress(destIp4));
		int[] cnts = new int[] { 5, 3, 4, 10 };
		long dateOffset = 10000;
		for (int i=0; i<cnts.length; i++) {
			for (int j= 0; j < cnts[i]; j++) {
				IpSession ip = new IpSession(this.factory);
				ip.setIpSrc(srcIpAddress);
				ip.setIpDest(addrs[i]);
				ip.setStartTime(dateOffset * (i + 1) * (j+1));
				template.save(ip);
			}
		}

		List<SessionSummary> ips = this.sessionRepository.getIpSessionSummaryByIpAddr(SessionQueryType.DEST, destIp1, 0, new Date().getTime());
		assertThat(ips.size(), is(1));
			SessionSummary s = ips.get(0);
			assertThat(s.getDestIpAddr(), is(destIp1));
			assertThat(s.getSrcIpAddr(), is(srcIp));
			assertThat(s.getNumSessions(), is((long)cnts[0]));
			assertThat(s.getEarliest(), is(dateOffset));
			assertThat(s.getLatest(), is(dateOffset * cnts[0]));

	}
}
