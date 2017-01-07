/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataSet implements Map<String,String>, Comparable<DataSet>  {
			
	
	private String id;
	private String source;
	private Date timestamp;
	private String type;
	private Map<String,String> map = new HashMap<String,String>();
	
	private static Calendar clock = Calendar.getInstance();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setTimestamp() {
		timestamp=clock.getTime();		
	}

	/*
	public String getValue(String key){
		return get(key);
	}
	*/
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(getId());
		str.append(System.lineSeparator());
		for (String key : this.keySet()){
			str.append(" ");
			str.append(key);
			str.append(':');
			str.append(get(key));
			//str.append(System.lineSeparator());
		}
		return str.toString();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int compareTo(DataSet dataSet) {
		return getId().compareTo(((DataSet) dataSet).getId());
	}

	// map
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public String get(Object key) {
		String value = map.get(key);
		if ((value==null) || value.equals("")) return Empty.string;
		if (value.equals("0")) return Empty.zero;	
		return value;
	}

	@Override
	public String put(String key, String value) {
		
		if ((value==null) || value.equals("")) value=Empty.string;
		else if (value.equals("0")) value=Empty.zero;	
		timestamp=clock.getTime();
		return map.put(key, value);
	}

	@Override
	public String remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();		
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<String> values() {
		return map.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return map.entrySet();		
	}
	
	public static DataSet merge(List<DataSet> dataSets){
		
		class ValueCount implements Comparable<ValueCount>{
			public int count=0;
			public String value=Empty.string;
			@Override
			public int compareTo(ValueCount o) {
				return count - o.count; 
			}
		}
			
		DataSet mergedDataSet = new DataSet();
		mergedDataSet.setId(dataSets.get(0).getId());
		mergedDataSet.setType(dataSets.get(0).getType());
		
		Set<String> keys = new HashSet<String>();
		for (DataSet dataSet:dataSets){
			keys.addAll(dataSet.keySet());
		}
		
		int i;
		int n=dataSets.size();
		ValueCount[] valueCounts = new ValueCount[n];

		for (String key : keys){
			
			for (i=0; i<n ; i++) valueCounts[i] = new ValueCount();
			
		    i=0;
			for (DataSet dataSet : dataSets)
				valueCounts[i++].value=dataSet.get(key);
			
			for (i=0; i<(n-1);i++)
				for (int j=i+1; j<n;j++)
					if (valueCounts[i].value.equals(valueCounts[j].value)) valueCounts[i].count++;
			
			Arrays.sort(valueCounts);
			
			String value=Empty.string;
			for (i=n-1; i>=0 ; i--){
				value = valueCounts[i].value;
				if (! (value==Empty.string || value==Empty.zero)) break;
			}
			
			mergedDataSet.put(key,value);
		}
		
		return mergedDataSet;
	
		
	}
}