/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib.datasource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSource;

public class TextFile implements DataSource {

	private String filename;
	private String source;
	private String name;
	private String type;

	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source){
		this.source=source;
	}

	public List<DataSet> getDataSetList() {
		List<DataSet> list = new LinkedList<DataSet>();
		List<String> keys = new LinkedList<String>();
		BufferedReader file;
		
		try {
			file = new BufferedReader ( new FileReader(filename));
			String line = file.readLine();
			StringTokenizer st = new StringTokenizer(line, "\t");
			st.nextToken();
			while (st.hasMoreTokens())
				keys.add(st.nextToken());
				
			while ((line=file.readLine()) != null ){
				DataSet dataSet = new DataSet();
				st = new StringTokenizer(line, "\t");
				dataSet.setId(st.nextToken());
				int i = 0;
				while (st.hasMoreTokens())
					dataSet.put(keys.get(i++),st.nextToken());
				list.add(dataSet);
			}
			file.close();
			
		} catch (IOException e) {
			System.out.println("eek");
		}
		Collections.sort(list);
		return list;
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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
	public void init() {
		// Nothing to init.		
	}

	@Override
	public Iterator<DataSet> getDataSetIterator() {
		
		return getDataSetList().iterator();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}	
}
