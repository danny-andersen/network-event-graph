package com.dsa.pcapneo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
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

	public void parseFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		parseFile(reader);
		reader.close();
	}
	
	private void parseFile(BufferedReader reader) {
		String line = null;
		int errors = 0, success = 0;
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
			} while (line != null);
		} catch (Exception e) {
			log.error("Exception whilst processing ", e);
		}
		log.info(String.format("Sessions imported: %d new sessions with %d failures", success, errors));
	}

	@Transactional	
	private boolean processLine(String line) {
		boolean success = false;
		try {
			PcapSummary pcap = new PcapSummary(line);
			Session session = sessionFactory.createSession(pcap);
			//Persist session
			template.save(session);
			success = true;
		} catch (Exception e) {
			log.error(String.format("Line failed: %s caused by: %s",line,e.getMessage()));
		}
		return success;
	}


}
