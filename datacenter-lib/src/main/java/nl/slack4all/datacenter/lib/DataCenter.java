/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.List;

public class DataCenter {
	
	private static CQLConnection cqlConnection;
	private static List<DataSource> dataSources;
	private static List<DataStore> copies;
	private static DataStore merged;
	private static Logger logger;
	

	public void init(){
		logger.log("datacenter.init");
		copies = Copier.getOldCopies(dataSources, cqlConnection);
		merged = Merger.getOldMerged(dataSources, cqlConnection);		
		logger.log("datacenter.init.done");
	}
	
	public void refresh(){
		logger.log("datacenter.refresh");
		copies = Copier.getNewCopies(dataSources, cqlConnection);
		merged = Merger.getNewMerged(dataSources, cqlConnection);		
		logger.log("datacenter.refresh.done");
	}
	
	
	
	public void destroy(){
		
	}

	public CQLConnection getCqlConnection() {
		return cqlConnection;
	}

	public void setCqlConnection(CQLConnection cqlConnection) {
		DataCenter.cqlConnection = cqlConnection;
	}

	public List<DataSource> getDatasources() {
		return dataSources;
	}

	public void setDataSources(List<DataSource> dataSources) {
		DataCenter.dataSources = dataSources;
	}

	public List<DataStore> getCopies() {
		return copies;
	}

	public void setCopies(List<DataStore> copies) {
		DataCenter.copies = copies;
	}

	public DataStore getMerged() {
		return merged;
	}

	public void setMerged(DataStore merged) {
		DataCenter.merged = merged;
	}	
	
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		DataCenter.logger = logger;
	}


}
