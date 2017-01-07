/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nl.slack4all.datacenter.lib.datastore.Cassandra;

public class Copier {
	
	public static List<DataStore> getNewCopies (List<DataSource> dataSources, CQLConnection cqlConnection){
		List<DataStore> copies = new LinkedList<DataStore>();
		Date version=Calendar.getInstance().getTime();
		for (DataSource dataSource:dataSources){
			Cassandra copy = new Cassandra();
			copy.setCqlConnection(cqlConnection);
			copy.setName(dataSource.getName());
			copy.setType(dataSource.getType());
			copy.setVersion(version);
			copy.init();

			Iterator<DataSet> dataSetIterator = dataSource.getDataSetIterator();
			while (dataSetIterator.hasNext()){
				DataSet dataSet=dataSetIterator.next();
				copy.putDataSet(dataSet);
			}
			copies.add(copy);
		}
		return copies;		
	}
	
	public static List<DataStore> getOldCopies (List<DataSource> dataSources, CQLConnection cqlConnection){
		List<DataStore> copies = new LinkedList<DataStore>();
		for (DataSource dataSource:dataSources){
			Cassandra copy = new Cassandra();
			copy.setCqlConnection(cqlConnection);
			copy.setName(dataSource.getName());
			copy.setType(dataSource.getType());
			copy.init();
			copies.add(copy);
		}
		return copies;		
	}
	
}
