/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.web;

import nl.slack4all.datacenter.lib.DataSet;

public class SearchResult {
	
	String id;
	String type;
	String displayName;
	String summary;
	
	SearchResult(DataSet dataSet){
		id=dataSet.getId();
		type=dataSet.getType();
		switch (dataSet.getType()) {
			case "server" :
				createServerResult(dataSet);
		}
		
	}

	private void createServerResult(DataSet dataSet) {
		displayName=id;
		StringBuilder summaryBuilder = new StringBuilder();
		summaryBuilder.append(dataSet.get("description"));
		summaryBuilder.append(", dtap:");
		summaryBuilder.append(dataSet.get("dtap"));
		summaryBuilder.append(", ");
		summaryBuilder.append(dataSet.get("operatingSystem"));
		summaryBuilder.append(", ");
		summaryBuilder.append(dataSet.get("ipAddress"));
		summary=summaryBuilder.toString();
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getSummary() {
		return summary;
	}

}
