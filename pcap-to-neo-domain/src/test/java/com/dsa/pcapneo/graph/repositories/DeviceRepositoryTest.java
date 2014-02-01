package com.dsa.pcapneo.graph.repositories;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.dsa.pcapneo.domain.graph.IpAddress;
import com.dsa.pcapneo.domain.graph.IpSession;
import com.dsa.pcapneo.domain.graph.Port;
import com.dsa.pcapneo.domain.graph.User;
import com.dsa.pcapneo.domain.graph.WebSite;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(("/testContext.xml"))
public class DeviceRepositoryTest {

	@Autowired
	DeviceRepository deviceRepository;
	@Autowired
	Neo4jTemplate template;
	@Autowired
	SessionArtefactFactory factory;

	@Test
	@Transactional
	public void findDevicesByType() {
		DeviceType laptop = template.save(new DeviceType("laptop"));
		Device device = new Device("test1", laptop, template.save(new User(
				"user1")));
		deviceRepository.save(device);
		device = new Device("test2", laptop, new User("user2"));
		deviceRepository.save(device);
		device = new Device("test3", new DeviceType("phone"), new User("user3"));
		deviceRepository.save(device);

		Iterable<Device> laptops = deviceRepository
				.findByDeviceTypeName("laptop");
		List<String> names = new ArrayList<String>();
		for (Device d : laptops) {
			names.add(d.getHostName());
			Assert.assertTrue(d.getDeviceType().getName() == null);
			template.fetch(d.getDeviceType());
			Assert.assertFalse(d.getDeviceType().getName() == null);
			assertThat(d.getDeviceType().getName(), is("laptop"));
		}
		assertThat(names.size(), is(2));
		assertThat(names, hasItems("test1", "test2"));

		DeviceType dt = deviceRepository.getDeviceType("test1");
		Assert.assertNotNull(dt);
		assertThat(dt.getName(), is("laptop"));
	}

	@Test
	@Transactional
	public void findDeviceById() {
		DeviceType laptop = template.save(new DeviceType("laptop"));
		Device device = new Device("test1", laptop, template.save(new User(
				"user1")));
		deviceRepository.save(device);
		Long deviceId = device.getDeviceId();
		device = new Device("test2", laptop, new User("user2"));
		deviceRepository.save(device);
		device = new Device("test3", new DeviceType("phone"), new User("user3"));
		deviceRepository.save(device);

		Device ret = deviceRepository.findByDeviceId(deviceId);
		assertThat(ret.getHostName(), is("test1"));
		Assert.assertTrue(ret.getDeviceType().getName() == null);
		template.fetch(ret.getDeviceType());
		Assert.assertFalse(ret.getDeviceType().getName() == null);
		assertThat(ret.getDeviceType().getName(), is("laptop"));
	}

	@Test
	@Transactional
	public void findDeviceUsers() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		User user2 = template.save(new User("user2"));
		device.addUser(user2);
		template.save(device);
		device = new Device("test2", template.save(new DeviceType("laptop")),
				user2);
		template.save(device);

		Iterable<User> users = deviceRepository.getUsersOfDevice("test1");
		List<String> names = new ArrayList<String>();
		for (User user : users) {
			names.add(user.getName());
		}
		assertThat(names.size(), is(2));
		assertThat(names, hasItems("user1", "user2"));
	}

	@Test
	@Transactional
	public void findWebsitesFromDevice() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		template.save(device);
		HttpSession http = new HttpSession(this.factory);
		http.setUri("http://www.facebook.com/friend/bill");
		http.setFromDevice(device);
		template.save(http);
		http = new HttpSession(this.factory);
		http.setUri("http://www.yahoo.com/mail");
		http.setFromDevice(device);
		template.save(http);

		// Find websites
		Iterable<WebSite> webSites = deviceRepository
				.getAllWebSitesVisitedByDevice("test1");
		List<String> sites = new ArrayList<String>();
		for (WebSite website : webSites) {
			sites.add(website.getAddress());
		}
		assertThat(sites.size(), is(2));
		assertThat(sites, hasItems("www.yahoo.com", "www.facebook.com"));
	}

	@Test
	@Transactional
	public void findWebsitesFromIpAddr() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		String ip1 = "192.168.1.1";
		IpAddress ip = template.save(new IpAddress(ip1));
		device.addIpAddr(ip);
		template.save(device);

		HttpSession http = new HttpSession(this.factory);
		http.setUri("http://www.facebook.com/friend/bill");
		http.setFromDevice(device);
		template.save(http);
		http = new HttpSession(this.factory);
		http.setUri("http://www.yahoo.com/mail");
		http.setFromDevice(device);
		template.save(http);

		// Find websitesfromipaddr
		Iterable<WebSite> webSites = deviceRepository
				.getAllWebSitesVisitedByIpAddr(ip1);
		List<String> sites = new ArrayList<String>();
		for (WebSite website : webSites) {
			sites.add(website.getAddress());
		}
		assertThat(sites.size(), is(2));
		assertThat(sites, hasItems("www.yahoo.com", "www.facebook.com"));
	}

	@Test
	@Transactional
	public void getDevicesUsingIpAddress() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"netbook")), template.save(new User("user1")));
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress ip = template.save(new IpAddress(ip1));
		device.addIpAddr(ip);
		template.save(device);
		dev2.addIpAddr(template.save(new IpAddress(ip2)));
		template.save(dev2);
		dev3.addIpAddr(ip);
		template.save(dev3);

		// Find devs using 192.168.1.
		Iterable<Device> devices = deviceRepository
				.getDevicesUsingIpAddress("192.168.1.1");
		List<String> devs = new ArrayList<String>();
		for (Device dev : devices) {
			devs.add(dev.getHostName());
		}
		assertThat(devs.size(), is(2));
		assertThat(devs, hasItems("test1", "test3"));
	}

	@Test
	@Transactional
	public void getDevicesUsingIpAddressNode() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"netbook")), template.save(new User("user1")));
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress ip = template.save(new IpAddress(ip1));
		device.addIpAddr(ip);
		template.save(device);
		dev2.addIpAddr(template.save(new IpAddress(ip2)));
		template.save(dev2);
		dev3.addIpAddr(ip);
		template.save(dev3);

		// Find devs using 192.168.1.
		Iterable<Device> devices = deviceRepository
				.getDevicesUsingIpAddressNode(ip);
		List<String> devs = new ArrayList<String>();
		for (Device dev : devices) {
			devs.add(dev.getHostName());
		}
		assertThat(devs.size(), is(2));
		assertThat(devs, hasItems("test1", "test3"));
	}

	@Test
	@Transactional
	public void getLocalDevices() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"netbook")), template.save(new User("user1")));
		String ip1 = "192.168.1.1";
		String ip2 = "192.168.1.2";
		IpAddress ip = template.save(new IpAddress(ip1));
		device.addIpAddr(ip);
		template.save(device);
		dev2.addIpAddr(template.save(new IpAddress(ip2)));
		template.save(dev2);
		dev3.addIpAddr(ip);
		template.save(dev3);

		// Find local devs
		Iterable<Device> devices = deviceRepository.getLocalDevices();
		List<String> devs = new ArrayList<String>();
		for (Device dev : devices) {
			devs.add(dev.getHostName());
		}
		assertThat(devs.size(), is(3));
		assertThat(devs, hasItems("test1", "test2", "test3"));
	}

	@Test
	@Transactional
	public void getRemoteDevices() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3", template.save(new DeviceType(
				"netbook")), template.save(new User("user1")));
		String ip1 = "31.31.31.31";
		String ip2 = "192.168.1.2";
		IpAddress ip = template.save(new IpAddress(ip1));
		device.addIpAddr(ip);
		template.save(device);
		dev2.addIpAddr(template.save(new IpAddress(ip2)));
		template.save(dev2);
		dev3.addIpAddr(ip);
		template.save(dev3);

		// Find local devs
		Iterable<Device> devices = deviceRepository.getRemoteDevices();
		List<String> devs = new ArrayList<String>();
		for (Device dev : devices) {
			devs.add(dev.getHostName());
		}
		assertThat(devs.size(), is(2));
		assertThat(devs, hasItems("test1", "test3"));
	}
	
	@Test
	@Transactional
	public void findDevicesByPort() {
		Device device = new Device("test1", template.save(new DeviceType(
				"laptop")), template.save(new User("user1")));
		Device dev2 = new Device("test2",
				template.save(new DeviceType("laptop")),
				template.save(new User("user1")));
		Device dev3 = new Device("test3",
				template.save(new DeviceType("laptop2")),
				template.save(new User("user1")));
		Device dev4 = new Device("test4",
				template.save(new DeviceType("laptop3")),
				template.save(new User("user1")));
		template.save(device);
		template.save(dev2);
		template.save(dev3);
		template.save(dev4);
		Port port = template.save(new Port(1000));
		Port port2 = template.save(new Port(2000));
		Port port3 = template.save(new Port(3000));

		IpSession ip = new IpSession(this.factory);
		ip.setDestPort(port);
		ip.setSrcPort(port2);
		ip.setFromDevice(device);
		ip.setToDevice(dev2);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		ip.setDestPort(port);
		ip.setSrcPort(port3);
		template.save(ip);
		ip = new IpSession(this.factory);
		ip.setToDevice(dev2);
		ip.setFromDevice(dev3);
		ip.setDestPort(port);
		ip.setSrcPort(port3);
		template.save(ip);
		Iterable<Map<String,Object>> res = deviceRepository.getDevicesUsingPort(port, 0, new Date().getTime());
		List<Device> devices = new ArrayList<Device>();
		List<Integer> sess = new ArrayList<Integer>();
		for (Map<String,Object> r : res) {
			Device dev = template.convert(r.get("device"), Device.class);
			devices.add(dev);
			sess.add(template.convert(r.get("numSessions"), Integer.class));
		}
		assertThat(devices.size(), is(3));
		assertThat(devices.get(0).getDeviceId(), is(dev2.getDeviceId()));
		assertThat(sess.get(0), is(3));
		assertThat(devices.get(1).getDeviceId(), is(dev3.getDeviceId()));
		assertThat(sess.get(1), is(2));
		assertThat(devices.get(2).getDeviceId(), is(device.getDeviceId()));
		assertThat(sess.get(2), is(1));
	}
}
