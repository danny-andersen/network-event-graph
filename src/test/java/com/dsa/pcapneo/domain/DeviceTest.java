package com.dsa.pcapneo.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/applicationContext.xml"))
public class DeviceTest extends AbstractTransactionalJUnit4SpringContextTests
{
	@Autowired Neo4jTemplate template;
	
	@Transactional
	@Test
	public void devicePersistence() {
		Device device = new Device("test1", new DeviceType("test"));
		Assert.assertNull(device.getDeviceId());
		template.save(device);
		Assert.assertNotNull(device.getDeviceId());
		//Check in graph
		Device retrieved = template.findOne(device.getDeviceId(), Device.class);
		Assert.assertEquals(retrieved, device);
	}

}
