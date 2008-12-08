package handler;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;
import merapi.Bridge;;

public class DataSourceHandler implements IMessageHandler {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	private String st = new String();
	
	@Override
	public void handleMessage(IMessage message) {
		try{
			st = (String)message.getData();
			String[] values = st.split("##");
			ds.setUsername(values[0]);
			ds.setPassword(values[1]);
			ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			ds.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
			testBase();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void testBase(){
		Message m;
		try {
			Connection con = ds.getConnection();
			m = new Message ("testBase",null,"Succes");
		} catch (SQLException e) {
			m = new Message ("testBase",null,e.getMessage());
		}
			try {
				Bridge.getInstance().sendMessage(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
