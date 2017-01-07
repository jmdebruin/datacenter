/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class JsonObjectFactory {
	
	private static String toString(Object object){
		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}";
		}
	}

	public static String toString(DataSet dataSet){
		return toString(object(dataSet));
	}

	public static String toString(List<DataSet> dataSets){
		return toString(object(dataSets));
	}
	
	public static Object object(DataSet dataSet){
		Map<String,Object> object = new HashMap<String,Object>();
		object.put("id", dataSet.getId());
		object.put("type", dataSet.getType());
		object.put("properties", dataSet.getMap());
		return object;
	}
	
	public static List<Object> object(List<DataSet> dataSets){
		List<Object> objects = new LinkedList<Object>();
		for (DataSet dataSet : dataSets){
			Map<String,Object> object = new HashMap<String,Object>();
			object.put("id", dataSet.getId());
			object.put("type", dataSet.getType());
			object.put("properties", dataSet.getMap());
			objects.add(object);
		}
		return objects;
	}
}
