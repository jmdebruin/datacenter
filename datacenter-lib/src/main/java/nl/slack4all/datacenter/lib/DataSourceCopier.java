package nl.slack4all.datacenter.lib;

import java.util.LinkedList;
import java.util.List;

import nl.slack4all.datacenter.lib.datastore.Cassandra;

public class DataSourceCopier {
	
	private DataCenter dataCenter;
	
	public List<DataStore> getCurrentCopies {
		List<DataStore> copies = new LinkedList<DataStore>();
		for (DataSource dataSource:datacenter.getDataSources()){
			Cassandra copy = new Cassandra();
			copy.setCqlConnection(cqlConnection);
			copy.setName(dataSource.getName());
			copy.setType(dataSource.getType());
			copy.init();
			copies.add(copy);
		}
		return copies;		
	}
	


}
