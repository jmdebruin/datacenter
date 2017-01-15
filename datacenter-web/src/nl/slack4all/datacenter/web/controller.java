/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */
package nl.slack4all.datacenter.web;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import nl.slack4all.datacenter.lib.DataCenter;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSource;
import nl.slack4all.datacenter.lib.Filter;
import nl.slack4all.datacenter.lib.DataStore;
import nl.slack4all.datacenter.lib.JsonObjectFactory;

@Controller
public class controller {
	
	@Autowired
	DataCenter dataCenter;
		
	@RequestMapping("/merged")
	public ModelAndView merged(){
		ModelAndView mvc = new ModelAndView("json");
		List<DataSet> servers = null;
		servers = dataCenter.getMerged().getDataSetList();
		mvc.addObject("message",servers);
		return mvc;
	}
	
	/*
	 * 	for ajax search box
	 */
		 
	@RequestMapping("/searchFor")
	public ModelAndView searchFor(@RequestParam("text") String text,@RequestParam(name="limit", defaultValue="20") String limit){
		int n;
		try {
			n=Integer.parseInt(limit);
		} catch (Exception e){
			n=20;
		};
		List<Object> searchResults= new LinkedList<Object>();
		for (DataSet dataSet : dataCenter.getMerged().getDataSetList(new Filter(null,null,text,n)))
			searchResults.add (JsonObjectFactory.object(dataSet));		
		ModelAndView mvc = new ModelAndView("json");
		mvc.addObject("message",searchResults);
		return mvc;
	}		 	

	@RequestMapping("/server")
	public ModelAndView restServer(@RequestParam("id") String id){				
		DataSet dataSet = dataCenter.getMerged().getDataSetById(id);
		ModelAndView mvc = new ModelAndView("json");
		mvc.addObject("message",JsonObjectFactory.object(dataSet));
		return mvc;
	}		 
	
	/*
	 *  for menu bar
	 */
	@RequestMapping("/datasources")
	public ModelAndView listDatasources(){
		ModelAndView mvc = new ModelAndView("default");
		String message = "";
		for (DataSource dataSource : dataCenter.getDatasources()){
			String textBlock = HtmlUtils.htmlEscape(dataSource.toString()).replaceAll("\n", "<br>\n");
			message += "<p>" +textBlock + "</p>";
		}
		mvc.addObject("message",message);
		return mvc;
	}
	
	@RequestMapping("/getCopies")
	public ModelAndView getCopies(){
		ModelAndView mvc = new ModelAndView("index");
		String message = "";
		for (DataStore dataStore : dataCenter.getCopies()){
			message += "<a /href=\"getDataSets?dataStoreName="+dataStore.getName()+"\">" + dataStore.getName() + "</a>; " + dataStore.getVersion() + "<br>";
		}
		mvc.addObject("message",message);
		return mvc;
	}
	
	@RequestMapping("/getDataSets")
	public ModelAndView getDataSets(@RequestParam("dataStoreName") String dataStoreName){
		List<DataSet> dataSets = null;
		for (DataStore dataStore : dataCenter.getCopies()){
			if (dataStore.getName().equals(dataStoreName)){
				dataSets=dataStore.getDataSetList();
			}
		}
		ModelAndView mvc = new ModelAndView("table");
		mvc.addObject("list",dataSets);
		return mvc;
	}		 
	
}


