package nl.slack4all.datacenter.lib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class Logger {
	
	int n=0;

	private CQLConnection cqlConnection;
	
	static Map<String,PreparedStatement> statements = new HashMap<String,PreparedStatement>();
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	public void log(String text){
		n = (n+1) % 255;
		Calendar calendar =  Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		Date dateTime=calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		
			
		String query="INSERT INTO logs (date,datetime,n,text) VALUES (?,?,?,?)";
		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(date,dateTime,n,text);
		cqlConnection.getSession().execute(boundStatement);		
	}
	
	public List<String> getLog(int days){
		
		List<String> lines = new LinkedList<String>();
		
		Calendar calendar =  Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		//calendar.add(Calendar.DAY_OF_MONTH, 0);
		calendar.add(Calendar.HOUR_OF_DAY, -2);
		Date dateTime=calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date = calendar.getTime();
		
		final String query="SELECT datetime,text FROM logs WHERE date=? AND datetime>=?;";

		PreparedStatement statement = statements.get(query);
		if (statement==null) statements.put(query,statement=cqlConnection.getSession().prepare(query));
		
		BoundStatement boundStatement = new BoundStatement(statement);
		boundStatement.bind(date, dateTime);
		ResultSet results = cqlConnection.getSession().execute(boundStatement);
		Row row = results.one();
		while (row !=null){
			lines.add(0,formatter.format(row.getTimestamp(0))+" : "+row.getString(1));
			row = results.one();
		}
		return lines;
			
	}

	public CQLConnection getCqlConnection() {
		return cqlConnection;
	}

	public void setCqlConnection(CQLConnection cqlConnection) {
		this.cqlConnection = cqlConnection;
	}

}
