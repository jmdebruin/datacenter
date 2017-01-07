package nl.slack4all.datacenter.test;

import java.util.Iterator;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import nl.slack4all.datacenter.lib.DataSet;
import nl.slack4all.datacenter.lib.DataSource;
import nl.slack4all.datacenter.lib.JsonObjectFactory;
import nl.slack4all.datacenter.lib.datasource.ActiveDirectory;

public class TestActiveDirectory {
	
	public static void main(String[] args){
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("ContextActiveDirectoryTest.xml");
		DataSource dataSource = (ActiveDirectory) ctx.getBean("active directory");
		
		Iterator<DataSet> dataSetIterator = dataSource.getDataSetIterator();
		
		while (dataSetIterator.hasNext()){
			DataSet dataSet=dataSetIterator.next();			
			System.out.println(JsonObjectFactory.toString(dataSet));
		}
				
		ctx.close();	
	}

}
