package com.dsa.pcapneo.domain;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
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
import com.dsa.pcapneo.domain.graph.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class DeviceTest //extends AbstractTransactionalJUnit4SpringContextTests
{
	@Autowired Neo4jTemplate template;
	
	@Transactional
	@Test
	public void devicePersistence() {
		Device device = new Device("test1", template.save(new DeviceType("test")), template.save(new User("user1")));
		Assert.assertNull(device.getDeviceId());
		template.save(device);
		Assert.assertNotNull(device.getDeviceId());
		//Check in graph
		Device retrieved = template.findOne(device.getDeviceId(), Device.class);
		Assert.assertEquals(retrieved, device);
		assertThat(template.fetch(retrieved.getUsers()).iterator().next().getName(), is("user1"));
		assertThat(template.fetch(retrieved.getDeviceType()).getName(), is("test"));
	}

	@Test
	public void tryNonTransactional() {
		Device device = new Device("test1", new DeviceType("test"), new User("user"));
		try {
			template.save(device);
			Assert.fail("Should have got an exception");
		} catch (Exception e) {
			
		}
	}
	
	@Transactional
	@Test
	public void deviceRelationships() {
		Device device = new Device("test1", new DeviceType("test"), new User("user"));
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		
		device.addIpAddr(template.save(new IpAddress(ip1)));
		device.addIpAddr(template.save(new IpAddress(ip2)));
		template.save(device);
		Device dev = template.findOne(device.getDeviceId(), Device.class);
		assertThat(dev.getHostName(), is("test1"));
		//Check type
		assertThat(template.fetch(dev.getDeviceType()).getName(), is("test"));
		//Check user
		Set<User> users = template.fetch(dev.getUsers());
		assertThat(users.size(), is(1));
		assertThat(users.iterator().next().getName(), is("user"));
		//Check ipaddrs
		Set<IpAddress> ips = dev.getIpaddr();
		assertThat(ips.size(), is(2));
		List<String> ipStrs = new ArrayList<String>(ips.size());
		for (IpAddress ip : ips) {
			ipStrs.add(ip.getIpAddr());
		}
		assertThat(ipStrs, hasItems(ip1, ip2));
	}
	

}
