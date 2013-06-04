package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

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
import com.dsa.pcapneo.domain.graph.HttpSession;
import com.dsa.pcapneo.domain.graph.User;
import com.dsa.pcapneo.domain.graph.WebSite;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/applicationContext.xml"))
public class DeviceRepositoryTest {
	
	@Autowired DeviceRepository deviceRepository;
	@Autowired Neo4jTemplate template;
	
	@Test
	@Transactional
	public void findDevicesByType() {
		DeviceType laptop = template.save(new DeviceType("laptop"));
		Device device = new Device("test1", laptop, template.save(new User("user1")));
		deviceRepository.save(device);
		device = new Device("test2", laptop, new User("user2"));
		deviceRepository.save(device);
		device = new Device("test3", new DeviceType("phone"), new User("user3"));
		deviceRepository.save(device);
		
		Iterable<Device> laptops = deviceRepository.findByDeviceTypeName("laptop");
		List<String> names = new ArrayList<String>();
		for (Device d : laptops) {
			names.add(d.getHostName());
		}
		assertThat(names.size(), is(2));
		assertThat(names, hasItems("test1", "test2"));

		DeviceType dt = deviceRepository.getDeviceType("test1");
		Assert.assertNotNull(dt);
		assertThat(dt.getName(), is("laptop"));
	}

	@Test
	@Transactional
	public void findDeviceUsers() {
		Device device = new Device("test1", template.save(new DeviceType("laptop")), template.save(new User("user1")));
		User user2 = template.save(new User("user2"));
		device.addUser(user2);
		template.save(device);
		device = new Device("test2", template.save(new DeviceType("laptop")), user2);
		template.save(device);
		
		Iterable<User> users = deviceRepository.getUsersOfDevice("test1");
		List<String> names = new ArrayList<String>();
		for (User user: users) {
			names.add(user.getName());
		}
		assertThat(names.size(), is(2));
		assertThat(names, hasItems("user1", "user2"));
	}
	
	@Test
	@Transactional
	public void findWebsitesFromDevice() {
		Device device = new Device("test1", template.save(new DeviceType("laptop")), template.save(new User("user1")));
		template.save(device);
		HttpSession http = new HttpSession("http://www.facebook.com/friend/bill");
		http.setDevice(device);
		template.save(http);
		http = new HttpSession("http://www.yahoo.com/mail");
		http.setDevice(device);
		template.save(http);
		
		//Find websites
		Iterable<WebSite> webSites = deviceRepository.getAllWebSitesVisitedByDevice("test1");
		List<String> sites = new ArrayList<String>();
		for (WebSite website : webSites) {
			sites.add(website.getAddress());
		}
		assertThat(sites.size(), is(2));
		assertThat(sites, hasItems("www.yahoo.com", "www.facebook.com"));
	}
}
