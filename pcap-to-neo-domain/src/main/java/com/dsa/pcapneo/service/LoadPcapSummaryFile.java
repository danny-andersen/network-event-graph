package com.dsa.pcapneo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
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
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("applicationContext.xml");
		final GraphDatabaseService graphDb = context.getBean(GraphDatabaseService.class);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.info("Closing down database cleanly");
				graphDb.shutdown();
			}
		});
		File inFile = new File(args[0]);
		LoadPcapSummaryFile load = new LoadPcapSummaryFile();
		load.parseFile(inFile);
		graphDb.shutdown();
	}
	
	public void parseFile(File file) throws IOException {
		ApplicationContext context =
			    new ClassPathXmlApplicationContext("applicationContext.xml");
		PcapSummaryLoadService graphInserter = context.getBean(PcapSummaryLoadService.class);
		GraphDatabaseService graphDb = context.getBean(GraphDatabaseService.class);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		graphInserter.parseFile(reader);
		reader.close();
	}
}
