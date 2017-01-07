/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CQLConnection {

	private String node;
	private String keyspace;
	
	private Cluster cluster;
	private Session session;
	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getKeyspace() {
		return keyspace;
	}
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}
	public Session getSession() {
		return session;
	}
	public void getSession(Session session) {
		this.session = session;
	}
	
	public void init(){
		cluster = Cluster.builder().addContactPoint(node).build();
		session = cluster.connect(keyspace);
	}
	
	public void destroy(){
		session.close();
		cluster.close();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
	}
	
	
	
}
