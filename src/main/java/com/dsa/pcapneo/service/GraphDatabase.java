package com.dsa.pcapneo.service;

import org.neo4j.graphdb.GraphDatabaseService;

public interface GraphDatabase {
	public GraphDatabaseService open();
	public void close();
//	public Node createNode(GraphProperties props);
}
