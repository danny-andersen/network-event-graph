package com.dsa.pcapneo.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoadPcapSummaryFile {
	private static final Log log = LogFactory.getLog(LoadPcapSummaryFile.class);
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			throw new IllegalArgumentException("Usage: <filename to read>");
		}
		File inFile = new File(args[0]);
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("applicationContext.xml");
		PcapSummaryLoadService graphInserter = context.getBean(PcapSummaryLoadService.class);
		graphInserter.parseFile(inFile);
	}
		

}
