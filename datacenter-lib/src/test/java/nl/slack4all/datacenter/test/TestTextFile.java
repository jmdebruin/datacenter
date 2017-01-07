/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.test;


public class TestTextFile {
	
	//@Before
	public void beforeEachTest() {
		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	}

	//@After
	public void afterEachTest() {
		System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	}
/*
	//@Test
	public void TextFileFindServers() {
		try {
			List<DataSet> list;
			int i=0;

			DataStore textFile = new TextFile();
			((TextFile) textFile).setFilename("src/test/resources/servers-file-1.txt");
		
			System.out.println("All servers");
			list = textFile.getDataSetList();
			for ( DataSet server : list){
				System.out.println(server.toString());
				i++;
			}
			System.out.println("n="+i);
			
			
			i=0;
			System.out.println("All servers with ipaddres 10.10.0.0/24");
			list = textFile.getDataSetList("ipaddress","10.10");
			for ( DataSet server : list){
				System.out.println(server.toString());
				i++;
			}
			System.out.println("n="+i);
			
			i=0;
			System.out.println("All servers with web");
			list = textFile.getDataSetList("web");
			for ( DataSet server : list){
				System.out.println(server.toString());
				i++;
			}
			System.out.println("n="+i);
			
		}
		catch (Exception e){
			fail(e.toString());
		}
		*/
	}

