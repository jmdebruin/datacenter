/*	Copyright (C) 2017 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.lib;

public class DataCenterRefreshTask extends Task{
	
	DataCenter datacenter;
	
	@Override
	protected void doRun(){
		datacenter.refresh();
	}

	public DataCenter getDatacenter() {
		return datacenter;
	}

	public void setDatacenter(DataCenter datacenter) {
		this.datacenter = datacenter;
	}

}
