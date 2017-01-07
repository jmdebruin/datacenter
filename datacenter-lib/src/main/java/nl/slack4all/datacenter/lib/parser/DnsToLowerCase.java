/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib.parser;

import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSetParser;
import nl.slack4all.datacenter.lib.Empty;
import nl.slack4all.datacenter.lib.LABEL;

public class DnsToLowerCase implements DataSetParser {

	private DataSetParser nextDataSetParser = null;
	
	@Override
	public DataSet parse(DataSet dataSet) {
		String hostname = dataSet.get(LABEL.hostname);
		if (hostname != Empty.string)
			dataSet.put(LABEL.hostname,hostname.toLowerCase());
		String domainName = dataSet.get(LABEL.domain_name);
		if (domainName != Empty.string)
			dataSet.put(LABEL.domain_name,domainName.toLowerCase());
		String fqdn = dataSet.get(LABEL.FQDN);
		if (fqdn != Empty.string)
			dataSet.put(LABEL.FQDN,fqdn.toLowerCase());
		if (nextDataSetParser == null)
			return dataSet;
		else
			return nextDataSetParser.parse(dataSet);
		
	}

	@Override
	public void setNextDataSetParser(DataSetParser dataSetParser) {
		this.nextDataSetParser=dataSetParser;
	}

}
