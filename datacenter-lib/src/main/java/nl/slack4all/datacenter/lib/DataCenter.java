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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCenter {
	
	
	private static final Logger logger = 
			LoggerFactory.getLogger(DataCenter.class);	
	
	private static CQLConnection cqlConnection;
	private static List<DataSource> dataSources;
	
	DataSourceCopier dataSourceCopier;
	private static List<DataStore> copies;
	private static DataStore merged;
	
	private static DataCenterRefreshTask dataCenterRefreshTask;
	private static int refreshInterval= 5 * 60 * 1000;
	

	public void init(){
		logger.info("datacenter.init");
		dataSourceCopier = new DataSourceCopier();
		dataSourceCopier.setCqlConnection(cqlConnection);
		dataSourceCopier.setDataSources(dataSources);
		copies = dataSourceCopier.getCopies();
		merged = Merger.getOldMerged(dataSources, cqlConnection);		
		dataCenterRefreshTask= new DataCenterRefreshTask();
		dataCenterRefreshTask.setDatacenter(this);
		dataCenterRefreshTask.setInterval(refreshInterval);
		dataCenterRefreshTask.start();
		logger.info("datacenter.init.done");
		
	}
	
	public void refresh(){
		logger.info("datacenter.refresh");
		cleanUp();
		copies = dataSourceCopier.makeNewCopies();
		merged = Merger.getNewMerged(dataSources, cqlConnection);		
		logger.info("datacenter.refresh.done");
	}
		
	public void destroy(){
		dataCenterRefreshTask.doStop();
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

	public void cleanUp() {
		for (DataStore dataStore:copies) {
			dataStore.deleteOldVersions();
		}
		merged.deleteOldVersions();		
	}	
	

}
