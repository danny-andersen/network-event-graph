package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dsa.pcapneo.domain.graph.Device;
import com.dsa.pcapneo.domain.graph.DeviceType;
import com.dsa.pcapneo.domain.graph.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/applicationContext.xml"))
public class DeviceRepositoryTest {
	
	@Autowired DeviceRepository deviceRepository;
	
	@Test
	@Transactional
	public void findDevicesByType() {
		Device device = new Device("test1", new DeviceType("laptop"), new User("user1"));
		deviceRepository.save(device);
		device = new Device("test2", new DeviceType("laptop"), new User("user2"));
		deviceRepository.save(device);
		device = new Device("test3", new DeviceType("phone"), new User("user3"));
		deviceRepository.save(device);
		
		Iterable<Device> laptops = deviceRepository.findByDeviceTypeName("laptop");
		List<String> names = new ArrayList<String>();
		for (Device laptop : laptops) {
			names.add(laptop.getHostName());
		}
		assertThat(names.size(), is(2));
		assertThat(names, hasItems("test1", "test2"));
	}

}
