/*
 *	Copyright (C) 2016 Hans de Bruin <j.m.debruin@slack4all.nl>
 *  
 *	This program is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU General Public License
 *	as published by the Free Software Foundation; either version 2
 *	of the License, or (at your option) any later version.
 */

/* Spring Bean Example:
 * 
 * <bean id="server in example.com"
 *		class="nl.slack4all.datacenter.lib.datasource.ACtiveDirectory"
 *		init-method="init"
 *		destroy-method="destroy" >
 *		<property name="name" value="servers in example.com" />
 *		<property name="type" value="servers" />
 *		<property name="url" value="ldap://example.com:389" />
 *		<property name="account"><ref bean="example.com account" /></property>
 *		<property name="searchFilter" value="(objectClass=computer)" />
 *		<property name="searchBase" value="ou=servers,dc=example.dc=com" />
 *		<property name="attributeMap">
 *			<map>
 *				<entry key="name" value="hostname" />    <--! this one is mandatory -->
 *				<entry key="dNSHostName" value="FQDN" /> <--! this one is mandatory -->
 *				<entry key="operatingSystem" value="operating system" />
 *				<entry key="operatingSystemVersion" value="operating system version" />
 *				<entry key="distinguishedName" value="distinguished name" />
 *				<entry key="division" value="division" />
 *			</map>
 *		</property>
 *	</bean>
 * 
 */

package nl.slack4all.datacenter.lib.datasource;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

import nl.slack4all.datacenter.lib.Account;
import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSetParser;
import nl.slack4all.datacenter.lib.DataSource;
import nl.slack4all.datacenter.lib.Empty;
import nl.slack4all.datacenter.lib.LABEL;
import nl.slack4all.datacenter.lib.parser.DnsToLowerCase;

public class ActiveDirectory implements DataSource {
	
	String name;
	String type;
	String url;
	Account account;
	String searchFilter;
	String searchBase;
	Map<String,String> attributeMap;
		
	Hashtable<String, String> environment;
	String domain;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public void setName(String name) {
		this.name=name;

	}

	@Override
	public void setType(String type) {
		this.type=type;

	}

	public String getSearchFilter() {
		return searchFilter;
	}

	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}

	public String getSearchBase() {
		return searchBase;
	}

	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase.toLowerCase();
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	@Override
	public void init() {
		 environment = new Hashtable<String, String>();
		 environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		 environment.put(Context.PROVIDER_URL,  url);
		 environment.put(Context.SECURITY_AUTHENTICATION, "simple");
		 environment.put(Context.SECURITY_PRINCIPAL, account.getUsername() +'@' + account.getDomain());
		 environment.put(Context.SECURITY_CREDENTIALS, account.getPassword());
		 
		 domain="";
		 int p = searchBase.indexOf("dc=")+3;
		 if (p != -1){
			 domain = searchBase.substring(p, searchBase.length());
			 domain = domain.replaceAll(",dc=", ".");
		 }
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public Iterator<DataSet> getDataSetIterator() {
		List<DataSet> dataSets = new LinkedList<DataSet>();
		DataSetParser dnsToLowercase = new DnsToLowerCase(); 
		try{
			int pageSize = 100; 
			byte[] cookie = null;
			
			LdapContext ldapContext = new InitialLdapContext(environment,null);
			ldapContext.setRequestControls(new Control[]{
					new PagedResultsControl(pageSize, Control.NONCRITICAL) });
			
			
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			searchControls.setReturningAttributes(attributeMap.keySet().toArray(new String[attributeMap.size()]));
			
			do {
				NamingEnumeration<SearchResult> searchResults = ldapContext.search(searchBase, searchFilter, searchControls);
			
				while (searchResults.hasMoreElements()){
					SearchResult searchResult = (SearchResult)searchResults.next();
					Attributes attributes = searchResult.getAttributes();
					
					DataSet dataSet = new DataSet();
					dataSet.setType(type);
	        				
					NamingEnumeration<String> keys = attributes.getIDs();
					while (keys.hasMore()){
						String key =keys.next(); 
						dataSet.put(attributeMap.get(key),  attributes.get(key).get().toString());
					}
					dnsToLowercase.parse(dataSet);
					String id = dataSet.get(LABEL.FQDN);
					if (id == Empty.string)
						id = dataSet.get("hostname") + "." + domain;
					dataSet.setId(id);
					dataSet.put(".id."+name,dataSet.get("distinguished name"));
					dataSets.add(dataSet);
				}
				
				Control[] controls = ldapContext.getResponseControls();
				if (controls != null)
					for (int i = 0; i < controls.length; i++)
						if (controls[i] instanceof PagedResultsResponseControl)
							cookie = ((PagedResultsResponseControl) controls[i]).getCookie();

				ldapContext.setRequestControls(new Control[] { new PagedResultsControl(
			            pageSize, cookie, Control.CRITICAL) });

			} while (cookie != null);

			ldapContext.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return dataSets.iterator();
	}

}
	
