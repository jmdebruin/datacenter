/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This code is licensed under the GPLv2 or later.
 */
package nl.slack4all.datacenter.lib.parser;

import nl.slack4all.datacenter.lib.DNSHelper;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSetParser;

/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This code is licensed under the GPLv2 or later.
 */
public class FixIdByDNS implements DataSetParser{

	private DNSHelper dnsHelper;
	private DataSetParser nextDataSetParser = null;
	
	@Override
	public DataSet parse(DataSet dataSet) {
		String id = dataSet.getId().toLowerCase();
		int firstDot = id.indexOf('.');
		if (firstDot==-1){
			id=id+'.'+dnsHelper.getDomain(id);
			dataSet.setId(id);
		}
		if (nextDataSetParser == null)
			return dataSet;
		else
			return nextDataSetParser.parse(dataSet);
	}

	public DNSHelper getDnsHelper() {
		return dnsHelper;
	}

	public void setDnsHelper(DNSHelper dnsHelper) {
		this.dnsHelper = dnsHelper;
	}

	@Override
	public void setNextDataSetParser(DataSetParser dataSetParser) {
		this.nextDataSetParser=dataSetParser;		
	}

}
