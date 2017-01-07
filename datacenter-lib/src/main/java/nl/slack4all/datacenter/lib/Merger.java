/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nl.slack4all.datacenter.lib.datastore.Cassandra;

public class Merger {
	
	
	public static DataStore getNewMerged (List<DataSource> dataSources, CQLConnection cqlConnection){
		Date version=Calendar.getInstance().getTime();
		DataStore merged = new Cassandra();
		merged.setCqlConnection(cqlConnection);
		merged.setName("merged");
		if (dataSources.size()>0)
			merged.setType(dataSources.get(0).getType());
		else
			merged.setType("unknown");
		merged.setVersion(version);
		merged.init();
		
		List<Iterator<DataSet>> iterators = new ArrayList<Iterator<DataSet>>();
		List<DataSet> dataSets = new ArrayList<DataSet>();
		List<DataSet> mergeSets = new LinkedList<DataSet>();
			
		for (DataSource source:dataSources){
			Iterator<DataSet> iterator = source.getDataSetIterator();
			if (iterator.hasNext()){
				iterators.add(iterator);
				dataSets.add(iterator.next());
			}
		}
		
		
		
		int n = iterators.size();

		while (n>0) {
			DataSet smallest = Collections.min(dataSets);

			System.out.print("iterators  :");
			for (Iterator<DataSet> item : iterators)
				System.out.print(item + "; ");
			System.out.println();
			System.out.print("datasets   :");
			for (DataSet item : dataSets)
				System.out.print(item.getId() + "; ");
			System.out.println();
			System.out.println("n          : "+String.valueOf(n));
			System.out.println("smallest   : "+smallest.getId());
			System.out.print("pulled     : ");
			
			for (int i=(n-1); i>=0 ; i--){
				if (smallest.compareTo(dataSets.get(i))==0){
					mergeSets.add(dataSets.get(i));
					System.out.print(i);
					if (iterators.get(i).hasNext())
						dataSets.set(i,iterators.get(i).next());
					else {
						iterators.remove(i);
						dataSets.remove(i);
						n--;
					}
				} else System.out.print('.');
			}
			merged.putDataSet(DataSet.merge(mergeSets));
			mergeSets.clear();
			System.out.println();
		} 
		
		return merged;
	}	
	
	public static DataStore getOldMerged (List<DataSource> dataSources, CQLConnection cqlConnection){
		DataStore merged = new Cassandra();
		merged.setCqlConnection(cqlConnection);
		merged.setName("merged");
		if (dataSources.size()>0)
			merged.setType(dataSources.get(0).getType());
		else
			merged.setType("unknown");
		merged.init();
		return merged;
	}
}
