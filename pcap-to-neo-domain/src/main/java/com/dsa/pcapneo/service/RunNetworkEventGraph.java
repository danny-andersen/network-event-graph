package com.dsa.pcapneo.service;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RunNetworkEventGraph {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("applicationContext.xml");
		//Get graph 
		LocalGraphDatabase db = context.getBean(LocalGraphDatabase.class);
		//Only returns on quit 
		db.startGraphDb();
	}

}
