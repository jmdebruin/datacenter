/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public interface DataStore extends DataSource{
	
	public Date getVersion();

	public void setVersion(Date version);
	
	public void deleteOldVersions();

	
	public DataSet 		     getDataSetById(String id);
	public List<DataSet>     getDataSetList();
	public List<DataSet>     getDataSetList(Filter filter);
	public List<DataSet>     getDataSetList(String text);
	public List<DataSet>     getDataSetList(String Key, String text);
	
	public Iterator<DataSet> getDataSetIterator();
	
	public void putDataSet(DataSet dataset);

	public void setCqlConnection(CQLConnection cqlConnection);

}
