package com.dsa.pcapneo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dsa.pcapneo.domain.graph.Session;
import com.dsa.pcapneo.domain.session.PcapSummary;
import com.dsa.pcapneo.domain.session.SessionFactory;

@Component
@Transactional
public class PcapSummaryLoadService {
	private static final Log log = LogFactory.getLog(PcapSummaryLoadService.class);

	@Autowired private SessionFactory sessionFactory;
	@Autowired private Neo4jTemplate template;
	@Autowired private GraphDatabaseService graphDb;

	public void parseFile(BufferedReader reader) {
		String line = null;
		int errors = 0, success = 0;
		long startTime = new Date().getTime();
		long lastTime = startTime;
		try {
			do {
				line = reader.readLine();
				if (line != null) {
					if (processLine(line)) {
						success++;
					} else {
						errors++;
					}
				}
				if (success % 100 == 0) {
					long currentTime = new Date().getTime();
					long elapsed = currentTime - startTime;
					long interval = currentTime - lastTime;
					log.info(String.format("Imported %d lines, had %d failures in %f secs, current rate: %f, avg rate: %f",
							success, errors, elapsed/1000.0, (1000*100.0/interval), (1000.0 * success / elapsed)));
					lastTime = currentTime;
				}
			} while (line != null);
		} catch (Exception e) {
			log.error("Exception whilst processing ", e);
		}
		long currentTime = new Date().getTime();
		long elapsed = currentTime - startTime;
		log.info(String.format("Sessions imported: %d new sessions with %d failures in %d secs at a rate of: %f/sec",
						success, errors, elapsed/1000, (1000.0*(success+errors)/elapsed)));
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)	
	private boolean processLine(String line) {
		boolean success = false;
		try {
			PcapSummary pcap = new PcapSummary(line);
			Session session = sessionFactory.createSession(pcap);
			//Persist session
			template.save(session);
			success = true;
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("Line failed: %s: ", e);
			}
			log.error(String.format("Line failed: %s caused by: %s",line,e.getMessage()),e);
		}
		return success;
	}


}
