/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.net.UnknownHostException;
import java.util.List;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

public class DNSHelper {
	
	private List<String> searchList;
	
	private Resolver resolver ;	

	public String getDomain(String hostname){		   
		
		for (String suffix : searchList){
			try {
			   Lookup lookup = new Lookup(hostname + "." + suffix, Type.A);
			   lookup.setResolver(resolver);
			   lookup.run();
			   if(lookup.getResult() == Lookup.SUCCESSFUL)
			   {
				   return suffix;
			   }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Empty.string;
	}
	
	public void init(){
		try {
			resolver =new SimpleResolver();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public List<String> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<String> searchList) {
		this.searchList = searchList;
	}
}
