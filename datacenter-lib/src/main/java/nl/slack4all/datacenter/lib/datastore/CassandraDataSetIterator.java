/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib.datastore;

import java.util.Iterator;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataStore;

public class CassandraDataSetIterator implements Iterator<DataSet> {

	

	ResultSet resultSet;
	DataStore datasoure;
	
	@Override
	public boolean hasNext() {
		return ! resultSet.isExhausted();
	}

	@Override
	public DataSet next() {
		DataSet dataSet = null;
		Row row = resultSet.one();
		if (row != null) {
			dataSet = new DataSet();
			dataSet.setId(row.getString(0));
			dataSet.setType(datasoure.getType());
			dataSet.setMap(row.getMap(1,String.class,String.class));
		}
		return dataSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet= resultSet;		
	}
	
	public void setDatasoure(DataStore datasoure) {
		this.datasoure = datasoure;
	}

}
