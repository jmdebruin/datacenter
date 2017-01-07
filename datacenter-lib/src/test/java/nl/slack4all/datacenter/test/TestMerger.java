/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.test;

import java.util.LinkedList;
import java.util.List;

import nl.slack4all.datacenter.lib.DataSet;

public class TestMerger {


	//@Test
	public static void runTest(){
		
		List<DataSet> list = new LinkedList<DataSet>();
		
		// 6 data sets
		
		DataSet A =new DataSet();	A.setId("Z");	list.add(A);
		DataSet B =new DataSet();	B.setId("Z");	list.add(B);
		DataSet C =new DataSet();	C.setId("Z");	list.add(C);
		DataSet D =new DataSet();	D.setId("Z");	list.add(D);
		DataSet E =new DataSet();	E.setId("Z");	list.add(E);

		A.put("k1", "aa");	B.put("k1", "aa");	C.put("k1", "aa");	D.put("k1", "aa");	E.put("k1", "aa");
		A.put("k2", "aa");	B.put("k2", "aa");	C.put("k2", "aa");	D.put("k2", "bb");	E.put("k2", "bb");
		A.put("k3", "bb");	B.put("k3", "bb");	C.put("k3", "aa");	D.put("k3", "aa");	E.put("k3", "aa");
		A.put("k4", "bb");	B.put("k4", "aa");	C.put("k4", "cc");	D.put("k4", "aa");	E.put("k4", "dd");
		A.put("k5", "");	B.put("k5", "aa");	C.put("k5", "");	D.put("k5", "");	E.put("k5", "");
		A.put("k6", "0");	B.put("k6", "aa");	C.put("k6", "0");	D.put("k6", "0");	E.put("k6", "0");
		A.put("k7", "aa");

		DataSet merged = DataSet.merge(list);
		System.out.println(merged.toString());
	}
	public static void main(String[] args){
		runTest();
	}
}
