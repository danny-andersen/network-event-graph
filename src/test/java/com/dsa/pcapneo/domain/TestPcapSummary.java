package com.dsa.pcapneo.domain;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.Session;
import com.dsa.pcapneo.domain.session.PcapSummary;
import com.dsa.pcapneo.domain.session.SessionArtefactFactory;
import com.dsa.pcapneo.domain.session.SessionFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/applicationContext.xml"))
public class TestPcapSummary extends TestCase {
	@Autowired Neo4jTemplate template;
	@Autowired SessionArtefactFactory factory;
	@Autowired SessionFactory sessionFactory;

	@Test
	@Transactional
	public void parseCsvString() {
		String pcapStr = "1367956351.145376000,eth:ip:tcp:http,192.168.1.79,72.21.214.199,433,6,38314,80,,,http://search.maven.org/,";
		PcapSummary pcap = null;
		try {
			pcap = new PcapSummary(pcapStr);
		} catch (Exception e) {
			Assert.fail("Failed to parse pcap str: " + e);
		}
		Session session = sessionFactory.createSession(pcap);
		//Persist session
		Long id = template.save(session).getSessionId();
		//Check is a http session
		assertEquals(session.getClass(), HttpSession.class);
		HttpSession ret = template.findOne(id, HttpSession.class);
		assertThat(ret.getDestPort().getPort(), is(80));
		assertThat(ret.getSrcPort().getPort(), is(38314));
		assertThat(ret.getDtoi(), is(1367956351L));
		assertThat(ret.getIpDest().getIpAddr(), is("72.21.214.199"));
		assertThat(ret.getIpSrc().getIpAddr(), is("192.168.1.79"));
		assertThat(ret.getLength(), is(433));
		//Check entities are as expected
		
	}

}
