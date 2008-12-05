package handler;

import java.sql.Connection;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;

public class DataSourceHandler implements IMessageHandler {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	private String st = new String();
	@Override
	public void handleMessage(IMessage message) {
		try{
			st = (String)message.getData();
			// PARSING
			String[] values = st.split("##");
			ds.setPassword(values[1]);
			ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			ds.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
			ds.setUsername(values[0]);
			
			Connection con = ds.getConnection();
			System.out.println(con.getMetaData());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
