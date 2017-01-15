package nl.slack4all.datacenter.lib;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nl.slack4all.datacenter.lib.datastore.Cassandra;

public class DataSourceCopier {
	
	private CQLConnection cqlConnection;
	private List<DataSource> dataSources;
	
	
	public CQLConnection getCqlConnection() {
		return cqlConnection;
	}

	public void setCqlConnection(CQLConnection cqlConnection) {
		this.cqlConnection = cqlConnection;
	}

	public List<DataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	public List<DataStore> getCopies() {
		List<DataStore> copies = new LinkedList<DataStore>();
		Date version=Calendar.getInstance().getTime();
		for (DataSource dataSource:dataSources){
			Cassandra copy = new Cassandra();
			copy.setCqlConnection(cqlConnection);
			copy.setName(dataSource.getName());
			copy.setType(dataSource.getType());
			copy.init();
			if (copy.getVersion() == Empty.version){
				copy.setVersion(version);
				copyDataSets(dataSource, copy);
			}
			copies.add(copy);
		}
		return copies;		
	}
	
	public List<DataStore> makeNewCopies (){
		List<DataStore> copies = new LinkedList<DataStore>();
		Date version=Calendar.getInstance().getTime();
		for (DataSource dataSource:dataSources){
			Cassandra copy = new Cassandra();
			copy.setCqlConnection(cqlConnection);
			copy.setName(dataSource.getName());
			copy.setType(dataSource.getType());
			copy.setVersion(version);
			copy.init();
			copyDataSets(dataSource, copy);
			copies.add(copy);
		}
		return copies;		
	}
		
	private void copyDataSets(DataSource dataSource, DataStore dataStore){
		Iterator<DataSet> dataSetIterator = dataSource.getDataSetIterator();
		while (dataSetIterator.hasNext()){
			DataSet dataSet=dataSetIterator.next();
			dataStore.putDataSet(dataSet);
		}
		
		
	}
	


}
