package com.dsa.pcapneo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dsa.pcapneo.domain.PcapSummary;

public class LoadPcapSummaryFile {
	private static final Log log = LogFactory.getLog(LoadPcapSummaryFile.class);
	protected Map<String, Integer> nodesByIp = new HashMap<String, Integer>();
	protected Map<Integer, StringBuffer> nodesById = new HashMap<Integer, StringBuffer>();
	protected List<String> edges = new ArrayList<String>();
	
	@Autowired
	private PcapSummaryFactory pcapSummaryFactory;
	
	@Autowired
	private GraphDatabase graphDb;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			throw new IllegalArgumentException("Usage: <filename to read> <filename to write>");
		}
		File inFile = new File(args[0]);
		File outFile = new File(args[1]);
		outFile.createNewFile();
		LoadPcapSummaryFile graphInserter = new LoadPcapSummaryFile();
		graphInserter.parseFile(inFile);
	}
	
	private void parseFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		do {
			line = reader.readLine();
			if (line == null) {
				break;
			}
			PcapSummary pcap = this.pcapSummaryFactory.createPcapSummary();
			try {
				pcap.parseCsvString(line);
			} catch (ParseException e) {
				reader.close();
				throw new IOException(e);
			}
//			pcap.insertIntoGraph(graphDb);
		} while (line != null);
		reader.close();
	}

	public void setPcapSummaryFactory(PcapSummaryFactory pcapSummaryFactory) {
		this.pcapSummaryFactory = pcapSummaryFactory;
	}
	

}
