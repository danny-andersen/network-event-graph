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

import com.dsa.pcapneo.domain.PcapSummary;

public class InsertGraphFromPcapFile {
	private static final Log log = LogFactory.getLog(InsertGraphFromPcapFile.class);
	protected Map<String, Integer> nodesByIp = new HashMap<String, Integer>();
	protected Map<Integer, StringBuffer> nodesById = new HashMap<Integer, StringBuffer>();
	protected List<String> edges = new ArrayList<String>();
	
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
		InsertGraphFromPcapFile graphInserter = new InsertGraphFromPcapFile();
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
			PcapSummary pcap = new PcapSummary();
			try {
				pcap.parseCsvString(line);
			} catch (ParseException e) {
				throw new IOException(e);
			}
			createNodes(pcap);
			createEdges(pcap);
		} while (line != null);
		reader.close();
	}

	private void createNodes(PcapSummary pcap) {
		
	}

	private void createEdges(PcapSummary pcap) {
		
	}
	

}
