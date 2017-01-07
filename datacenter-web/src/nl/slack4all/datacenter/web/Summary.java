/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.web;

import java.util.LinkedList;
import java.util.List;

import nl.slack4all.datacenter.lib.DataSet;

public class Summary {
	
	String sepperator=", ";

	String id;
	String type;
	String name;
	String text;
	
	public Summary(DataSet dataSet){
		id = dataSet.getId();
		type= dataSet.getType();
		name= dataSet.get("name");
		switch (type){
		case "server" :
			text=getServerText(dataSet);
			break;
		}
	}

	private String getServerText(DataSet dataSet) {
		StringBuilder txt = new StringBuilder();
		List<String> keys = new LinkedList<String>();
		keys.add("description");
		keys.add("operatingSystem");
		keys.add("memmmory MB");
		
		
		txt.append(dataSet.get("description"));
		//txt.append(c)
		return null;
	}
}
