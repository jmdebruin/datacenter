/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib.datastore;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import nl.slack4all.datacenter.lib.CQLConnection;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataStore;
import nl.slack4all.datacenter.lib.Empty;
import nl.slack4all.datacenter.lib.Filter;

public class Cassandra implements DataStore {

	private CQLConnection cqlConnection;
	private Date version;
	private String name;
	private String type;
	
	static Map<String,PreparedStatement> statements = new HashMap<String,PreparedStatement>();
	
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
		final String query="INSERT INTO datasources (datasource,type,version) VALUES (?,?,?)";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version);
		cqlConnection.getSession().execute(boundStatement);		
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Iterator<DataSet> getDataSetIterator() {
		
		final String query="SELECT id,dataset FROM datasets WHERE datasource=? AND type=? AND version=? ;";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version);
		ResultSet results = cqlConnection.getSession().execute(boundStatement);
		 
		CassandraDataSetIterator dataSetIterator = new CassandraDataSetIterator();
		dataSetIterator.setResultSet(results);
		dataSetIterator.setDatasoure(this);
				
		return dataSetIterator;
	}

	@Override
	public DataSet getDataSetById(String id) {
		
		final String query="SELECT dataset FROM datasets WHERE datasource=? AND type=? AND version=? AND id=? ;";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version, id);
		ResultSet results = cqlConnection.getSession().execute(boundStatement);
		Row row = results.one();
		if (row != null) {
			DataSet dataSet = new DataSet();
			dataSet.setId(id);
			dataSet.setType(getType());
			dataSet.setMap(row.getMap(0,String.class,String.class));
			return dataSet;
		}
		else
			return null;
	}
	
	@Override
	public List<DataSet> getDataSetList() {
		
		final String query="SELECT id,dataset FROM datasets WHERE datasource=? AND type=? AND version=? ;";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		
		List<DataSet> dataSets = new LinkedList<DataSet>();

		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version);
		ResultSet results = cqlConnection.getSession().execute(boundStatement);
		 
		for (Row row : results) {
			DataSet dataSet = new DataSet();
			dataSet.setId(row.getString(0));
			
			dataSet.setType(getType());
			dataSet.setMap(row.getMap(1,String.class,String.class));
			dataSets.add(dataSet);
		}
			
		return dataSets;
		
	}	

	@Override
	public List<DataSet> getDataSetList(String key, String text) {
		
		final String query="SELECT id,dataset FROM datasets WHERE datasource=? AND type=? AND version=? ;";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		
		List<DataSet> dataSets = new LinkedList<DataSet>();
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version);
		ResultSet results = cqlConnection.getSession().execute(boundStatement);
		for (Row row : results) {
			DataSet dataSet = new DataSet();
			dataSet.setMap(row.getMap(1,String.class,String.class));
			System.out.println(row.getString(0));
			System.out.println(dataSet.getMap().toString());
			if ( dataSet.get(key).contains(text)){
				dataSet.setId(row.getString(0));
				dataSet.setType(getType());
				dataSets.add(dataSet);
			}
		}
		return dataSets;
	}
	
	@Override
	public List<DataSet> getDataSetList(String text) {
		final String query="SELECT id,dataset FROM datasets WHERE datasource=? AND type=? AND version=?;";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		
		List<DataSet> dataSets = new LinkedList<DataSet>();
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version);
		ResultSet results = cqlConnection.getSession().execute(boundStatement);
		for (Row row : results) {
			DataSet dataSet = new DataSet();
			dataSet.putAll(row.getMap(1,String.class,String.class));
			for (String key : dataSet.keySet())
				if ( dataSet.get(key).contains(text) ){
					dataSet.setId(row.getString(0));
					dataSet.setType(getType());
					dataSets.add(dataSet);				
					break;
			}
		}
		return dataSets;
	}

	
	@Override
	public void putDataSet(DataSet dataSet) {
		
		final String query="INSERT INTO datasets (datasource,type,version,id,dataset) VALUES (?,?,?,?,?);";
		
		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));

		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version, dataSet.getId(),dataSet);
		cqlConnection.getSession().execute(boundStatement);
	}
	

	public CQLConnection getCqlConnection() {
		return cqlConnection;
	}

	public void setCqlConnection(CQLConnection cqlConnection) {
		this.cqlConnection = cqlConnection;
	}

	@Override
	public void init() {
		
		if (version == null) 
			version=readVersion();
	}
	
	private Date readVersion(){
		Date version = Empty.version;
		final String query="SELECT MAX(version) from datasources WHERE datasource=? AND type=? ;";
		
		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));	
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType());
		ResultSet results = cqlConnection.getSession().execute(boundStatement);;
		
		Row row = results.one();
		
		if (row != null && ! row.isNull(0))
			version = row.getTimestamp(0);
		
		return version;
	}
	
	private Date readOldVersion(){
		Date version = Empty.version;
		final String query="SELECT Min(version) from datasources WHERE datasource=? AND type=? ;";
		
		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));	
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType());
		ResultSet results = cqlConnection.getSession().execute(boundStatement);;
		
		Row row = results.one();
		
		if (row != null && ! row.isNull(0))
			version = row.getTimestamp(0);
		
		return version;
	}
	
	@Override
	public List<DataSet> getDataSetList(Filter filter) {
		
		final String query="SELECT id,dataset FROM datasets WHERE datasource=? AND type=? AND version=? ;";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		
		List<DataSet> dataSets = new LinkedList<DataSet>();
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(getName(), getType(), version);
		ResultSet results = cqlConnection.getSession().execute(boundStatement);
		 
		for (Row row : results) {
			DataSet dataSet = new DataSet();
			dataSet.setId(row.getString(0));
			
			dataSet.setType(getType());
			dataSet.setMap(row.getMap(1,String.class,String.class));
			if (filter.pass(dataSet))
				dataSets.add(dataSet);
		}
			
		return dataSets;
		
	}
	
	@Override
	public void deleteOldVersions(){

		String query1 = "DELETE FROM datasources WHERE datasource=? AND type=? AND version=? ;";
		PreparedStatement statement1 = statements.get(query1);
		if (statement1==null) statements.put(query1,statement1=cqlConnection.getSession().prepare(query1));
		BoundStatement boundStatement1 = new BoundStatement(statement1);
		
		final String query2="DELETE FROM datasets WHERE datasource=? AND type=? AND version=? ;";	
		PreparedStatement statement2 = statements.get(query2);
		if (statement2==null) statements.put(query2,statement2=cqlConnection.getSession().prepare(query2));
		BoundStatement boundStatement2 = new BoundStatement(statement2);
		
		Date oldVersion=readOldVersion();
		while (version.compareTo(oldVersion)>0){
			boundStatement1.bind(getName(), getType(), oldVersion);
			boundStatement2.bind(getName(), getType(), oldVersion);
			cqlConnection.getSession().execute(boundStatement1);
			cqlConnection.getSession().execute(boundStatement2);
			oldVersion=readOldVersion();
		}		
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type=type;		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
