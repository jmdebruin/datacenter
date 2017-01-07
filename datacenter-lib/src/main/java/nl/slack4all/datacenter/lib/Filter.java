/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

public class Filter {
	
	String key = null;
	String text = null;
	String type = null;
	int n=0;
	int i=0;
	
	public Filter(String type, String key, String text, int n){
		setType(type);
		setKey(key);
		setText(text);
		setN(n);
	}
	
	public boolean pass(DataSet dataSet) {
		boolean result= true;
		result = result && (n<=0 || i<n);
		result = result && (type == null || type.equals(dataSet.getType()));
		if (text != null){
			if (key != null)
				result = result && dataSet.get(key).contains(text);		
		    else {
		    	boolean contains = false;
		    	for (String key : dataSet.keySet())
		    		if ((key.charAt(0)!='.') 
		    				&& (dataSet.get(key).toLowerCase().contains(text))){
		    			contains = true;
		    			break;
		    		}
		    	result = result && contains;
		    }
		}
		if (result)
			i++;
		return result;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = getSafeString(type);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = getSafeString(text);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = getSafeString(type);
	}
	
	private String getSafeString(String string){
		if (string == null) return null;
		if (string.equals("")) return null;
		return string.toLowerCase();		
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}	
}
