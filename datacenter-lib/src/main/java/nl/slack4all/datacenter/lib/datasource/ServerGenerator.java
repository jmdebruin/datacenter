/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib.datasource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSource;
import nl.slack4all.datacenter.lib.Dictionary;
import nl.slack4all.datacenter.lib.Item;

public class ServerGenerator implements DataSource{
	
	private Item[] osArray;
	private Item[] typeArray;
	private Item[] domainArray;
	private Item[] dtapArray;
	private String name;
	private String type;
	private int N=100; 	
	private List<DataSet> servers;
	
	@Override
	public String getName() {
		return name;
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
	public String getType() {
		return type;
	}

	@Override
	public void init() {
		//Calendar.getInstance().getTime();
		type = "server";
		servers = new ArrayList<DataSet>(N);
		Random rnd = new Random(1259840938);
		for (int i=0;i<N;i++){
			DataSet dataSet = new DataSet();
			dataSet.setType("server");
			int osPtr=rnd.nextInt(osArray.length);
			int domainPtr=rnd.nextInt(domainArray.length);
			int typePtr=rnd.nextInt(typeArray.length);
			int dtapPtr=rnd.nextInt(dtapArray.length);
			int cpus=2*(rnd.nextInt(4)+1);
			int memory=1024*(rnd.nextInt(8)+1);
			String hostname="s" + String.format("%04d", i ) 
					+ "-" + osArray[osPtr].getAbbreviation()
					+ "-" + typeArray[typePtr].getAbbreviation()
					+ "-" + dtapArray[dtapPtr].getAbbreviation();
			String domain = domainArray[domainPtr].getName();
			dataSet.put("hostname", hostname);
			dataSet.put("domain", domain);
			dataSet.put("fqdn", hostname+"."+domain);
			dataSet.put("operatingsystem",Dictionary.lookup(osArray[osPtr].getName()));
			dataSet.put("dtap",Dictionary.lookup(dtapArray[dtapPtr].getName()));
			dataSet.setId(hostname+"."+domain);
			dataSet.put("ipAddress", "10.10."+domainPtr+"."+(i+1));
			String description =
					osArray[osPtr].getName()
					+ " " + typeArray[typePtr].getName()
					+ " for " + dtapArray[dtapPtr].getName()
					+ " on the " + domainArray[domainPtr].getAbbreviation()
					+ " network";
			dataSet.put("description", description);
			dataSet.put("memory MB",String.valueOf(memory));
			dataSet.put("cpu's",String.valueOf(cpus));
			dataSet.put(".id", String.valueOf(i));
			servers.add(i,dataSet);
		}
	}

	public void destroy() {
	}

	public Item[] getOsArray() {
		return osArray;
	}

	public void setOsArray(Item[] osArray) {
		this.osArray = osArray;
	}

	public Item[] getTypeArray() {
		return typeArray;
	}

	public void setTypeArray(Item[] typeArray) {
		this.typeArray = typeArray;
	}

	public Item[] getDomainArray() {
		return domainArray;
	}

	public void setDomainArray(Item[] domainArray) {
		this.domainArray = domainArray;
	}

	public Item[] getDtapArray() {
		return dtapArray;
	}

	public void setDtapArray(Item[] dtapArray) {
		this.dtapArray = dtapArray;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	@Override
	public Iterator<DataSet> getDataSetIterator() {
		return servers.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("datasource:").append(getName());
		
		return stringBuilder.toString();
	}
}
