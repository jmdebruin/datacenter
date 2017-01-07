/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {
	
	private static Map<String,String> table = new HashMap<String,String>(); 
	
	public static String lookup(String text){
		if (table.containsKey(text))
			return table.get(text);
		else
			return text;
	}
	
	public void addEntry(List<String> list){
		String word=list.get(0);
		table.put(word,word);
		for (String alias : list)
			table.put(alias,word);
	}
	
	public void setList(List<List<String>> lists){
		for (List<String> list : lists)
			addEntry(list);
	}
}
