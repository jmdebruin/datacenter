/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nl.slack4all.datacenter.lib.Account;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSetParser;
import nl.slack4all.datacenter.lib.DataSource;

public class SQL implements DataSource{
	
	// public properties
	private String name;
	private String type;
	private String driver;
	private String url;
	private Account account;
	private String query;
	private DataSetParser dataSetParser;
	
	// private properties
	private Connection connection;
	private String sqlConnectionString;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setName(String name) {
		this.name=name;		
	}

	@Override
	public void setType(String type) {
		this.type=type;
		
	}

	@Override
	public void init() {
		try {
			Class.forName(driver);
			sqlConnectionString=url+";user="+account.getUsername()+";password="+account.getPassword();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<DataSet> getDataSetIterator() {
		List<DataSet> dataSets = new LinkedList<DataSet>();	
		try{
			connection = DriverManager.getConnection(sqlConnectionString);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData metaData = rs.getMetaData();
			int n=metaData.getColumnCount();		
			while (rs.next()) {
				DataSet dataSet = new DataSet();
				dataSet.setId(rs.getString(1));
				for (int i=2 ; i<=n ; i++)
					dataSet.put(metaData.getColumnName(i), rs.getString(i));
				dataSets.add(dataSetParser.parse(dataSet));
			}
			rs.close();				
			connection.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return dataSets.iterator();
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
		
	public void setAccount(Account account) {
		this.account = account;
	}

	public DataSetParser getDataSetParser() {
		return dataSetParser;
	}

	public void setDataSetParser(DataSetParser dataSetParser) {
		this.dataSetParser = dataSetParser;
	}

}
