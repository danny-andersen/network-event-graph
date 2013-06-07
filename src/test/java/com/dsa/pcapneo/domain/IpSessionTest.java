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
import com.dsa.pcapneo.domain.session.SessionArtefactFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/applicationContext.xml"))
public class IpSessionTest {
	@Autowired Neo4jTemplate template;
	@Autowired SessionArtefactFactory factory;

	@Test
	@Transactional
	public void createIpSession() {
		IpSession session = new IpSession();
		String[] protos = new String[] {"tcp", "ip"};
		session.setProtocols(protos);
		session.setStartTime(new Date().getTime());
		session.setTransport("tcp");
		session.setLength(1024);
		Long sessionId = template.save(session).getSessionId();
		Assert.assertNotNull(sessionId);
		IpSession retrieved = template.findOne(sessionId, IpSession.class);
		Assert.assertNotNull(retrieved);
		assertThat(retrieved.getLength(), is(1024));
		assertThat(retrieved.getTransport(), is("tcp"));
		List<String> rproto = new ArrayList<String>();
		for (String p : retrieved.getProtocols()) {
			rproto.add(p);
		}
		assertThat(rproto, hasItems(protos));
	}

	@Test
	@Transactional
	public void createIpSessionWithOneRelationship() {
		IpSession session = new IpSession();
		session.setStartTime(new Date().getTime());
		session.setTransport("tcp");
		session.setLength(1024);
		IpAddress ip = new IpAddress("192.168.1.1");
		ip = template.save(ip);
		session.setIpSrc(ip);
		Long sessionId = template.save(session).getSessionId();
		Assert.assertNotNull(sessionId);
		IpSession retrieved = template.findOne(sessionId, IpSession.class);
		Assert.assertNotNull(retrieved);
		assertThat(retrieved.getLength(), is(1024));
		assertThat(retrieved.getTransport(), is("tcp"));
		assertThat(retrieved.getIpSrc().getIpAddr(), is("192.168.1.1"));
	}

	@Test
	@Transactional
	public void createIpSessionWithRelationships() {
		IpSession session = new IpSession();
		String[] protos = new String[] {"tcp", "ip"};
		session.setProtocols(protos);
		session.setStartTime(new Date().getTime());
		session.setTransport("tcp");
		session.setLength(1024);
		session.setIpSrc(factory.getIpAddress("192.168.1.1"));
		session.setIpDest(factory.getIpAddress("12.1.1.1"));
		session.setSrcPort(factory.getPort("24555"));
		session.setDestPort(factory.getPort("80"));
		Long sessionId = template.save(session).getSessionId();
		Assert.assertNotNull(sessionId);
		IpSession retrieved = template.findOne(sessionId, IpSession.class);
		Assert.assertNotNull(retrieved);
		assertThat(retrieved.getLength(), is(1024));
		assertThat(retrieved.getTransport(), is("tcp"));
		List<String> rproto = new ArrayList<String>();
		for (String p : retrieved.getProtocols()) {
			rproto.add(p);
		}
		assertThat(rproto, hasItems(protos));
		assertThat(retrieved.getIpSrc().getIpAddr(), is("192.168.1.1"));
		assertThat(retrieved.getIpDest().getIpAddr(), is("12.1.1.1"));
		assertThat(retrieved.getSrcPort().getPort(), is(24555));
		assertThat(retrieved.getDestPort().getPort(), is(80));
	}
}
