package com.dsa.pcapneo.domain;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Protocol;
import com.dsa.pcapneo.graph.repositories.SessionArtefactFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class IpSessionTest {
	@Autowired Neo4jTemplate template;
	@Autowired SessionArtefactFactory factory;

	@Test
	@Transactional
	public void createIpSession() {
		IpSession session = new IpSession();
		session.setStartTime(new Date().getTime());
		session.setProtocolNumber(17);
		session.setLength(1024);
		Long sessionId = template.save(session).getSessionId();
		Assert.assertNotNull(sessionId);
		IpSession retrieved = template.findOne(sessionId, IpSession.class);
		Assert.assertNotNull(retrieved);
		assertThat(retrieved.getLength(), is(1024));
		assertThat(retrieved.getProtocolNumber(), is(17));
		List<String> rproto = new ArrayList<String>();
	}

	@Test
	@Transactional
	public void createIpSessionWithOneRelationship() {
		IpSession session = new IpSession();
		session.setStartTime(new Date().getTime());
		session.setProtocolNumber(17);
		session.setLength(1024);
		IpAddress ip = new IpAddress("192.168.1.1");
		ip = template.save(ip);
		session.setSrcIp(ip);
		Long sessionId = template.save(session).getSessionId();
		Assert.assertNotNull(sessionId);
		IpSession retrieved = template.findOne(sessionId, IpSession.class);
		Assert.assertNotNull(retrieved);
		assertThat(retrieved.getLength(), is(1024));
		assertThat(retrieved.getProtocolNumber(), is(17));
		assertThat(retrieved.getSrcIp().getIpAddr(), is("192.168.1.1"));
	}

	@Test
	@Transactional
	public void createIpSessionWithRelationships() {
		IpSession session = new IpSession();
		String[] protos = new String[] {"tcp", "ip"};
		session.setProtocols(factory.getProtocols(protos));
		session.setStartTime(new Date().getTime());
		session.setProtocolNumber(17);
		session.setLength(1024);
		session.setSrcIp(factory.getIpAddress("192.168.1.1"));
		session.setIpDest(factory.getIpAddress("12.1.1.1"));
		session.setSrcPort(factory.getPort("24555"));
		session.setDestPort(factory.getPort("80"));
		Long sessionId = template.save(session).getSessionId();
		Assert.assertNotNull(sessionId);
		IpSession retrieved = template.findOne(sessionId, IpSession.class);
		Assert.assertNotNull(retrieved);
		assertThat(retrieved.getLength(), is(1024));
		assertThat(retrieved.getProtocolNumber(), is(17));
		List<String> rproto = new ArrayList<String>();
		for (Protocol p : template.fetch(retrieved.getProtocols())) {
			rproto.add(p.getName());
		}
		assertThat(rproto, hasItems(protos));
		assertThat(retrieved.getSrcIp().getIpAddr(), is("192.168.1.1"));
		assertThat(retrieved.getIpDest().getIpAddr(), is("12.1.1.1"));
		assertThat(retrieved.getSrcIp().getLocation().name(), is("LOCAL"));
		assertThat(retrieved.getIpDest().getLocation().name(), is("REMOTE"));
		assertThat(retrieved.getSrcPort().getPort(), is(24555));
		assertThat(retrieved.getDestPort().getPort(), is(80));
	}
}
