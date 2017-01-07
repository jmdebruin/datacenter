/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import nl.slack4all.datacenter.lib.DNSHelper;
import nl.slack4all.datacenter.lib.DataCenter;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.JsonObjectFactory;
import nl.slack4all.datacenter.lib.Logger;

public class TestTest {
	
	public ClassPathXmlApplicationContext ctx;
	public DataCenter dataCenter ;
		
	public void init(){
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		dataCenter = (DataCenter) ctx.getBean("dataCenter");		
	}

	public void destroyTest(){
		ctx.close();
	}

	public void runTest2(){
		
		Iterator<DataSet> serverIterator = dataCenter.getMerged().getDataSetIterator();
		System.out.println("------------------------------------------------------------------------");
		while (serverIterator.hasNext()){
			DataSet server = serverIterator.next();
				System.out.println( JsonObjectFactory.toString(server));
		}
			//System.out.println(Json.fromDataSet(server));
			
	}
	
	public void runTest3(){
		
		System.out.println("------------------------------------------------------------------------");
		
		DNSHelper dns = new DNSHelper();
		List<String> searchList = new LinkedList<String>();
		searchList.add("slack4all.nl");
		searchList.add("system");
		dns.setSearchList(searchList);
		dns.init();
		
		System.out.println(dns.getDomain("orion"));
				
		}
	

	
	public static void main(String[] args){
		TestTest main= new TestTest();
		
		main.init();
		
		//main.runTest3();
		
		//main.runTest2();
		
		//main.dataCenter.refresh();
		
		//main.runTest2();
		
		main.destroyTest();
		
				
	}
}
