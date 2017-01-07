/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib.datasource;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSource;

public class ServerInator implements DataSource {
	
	private DataSource datasource;
	private int[] rnd;
	private int seed = 15438;
	private String name;
	private String type;


	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;

	}

	@Override
	public void init() {
		Random generator = new Random(seed);
		int n = 1024;
		rnd = new int[n];
		for (int i=0; i<n ;i++)
			rnd[i]= generator.nextInt(1000);
	}

	public List<DataSet> getDataSetList() {		
		int i=0;
		List<DataSet> list = new LinkedList<DataSet>();
		Iterator<DataSet> iterator = datasource.getDataSetIterator();
		while (iterator.hasNext()){
			if (rnd[i]< 400){
				DataSet dataSet = iterator.next();
				dataSet.put(".id."+name, dataSet.getId());
				list.add(dataSet);
			}
			else
				iterator.next();			
			i = i+1; //(i+1) % rnd.length;
		}
		return list;
	}

	@Override
	public Iterator<DataSet> getDataSetIterator() {
		return getDataSetList().iterator();
	}

	public void destroy() {
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
		setType(datasource.getType());
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public void setType(String type) {
		this.type=type;
		
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("datasource: ").append(getName());
		stringBuilder.append("\noriginal datasource: ").append(getDatasource().getName());
		stringBuilder.append("\nseed: ").append(getSeed());
		return stringBuilder.toString();
	}
}
