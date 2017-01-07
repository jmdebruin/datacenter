/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.Iterator;

public interface DataSource {
	public String getName();
	public String getType();

	public void setName(String name);
	public void setType(String type);
	
	public void init();
	public void destroy();
	
	public Iterator<DataSet> getDataSetIterator();	
	
	public String toString();
}
