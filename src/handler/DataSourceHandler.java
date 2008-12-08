package handler;
import java.sql.SQLException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.DataSourceFactory;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;
import merapi.Bridge;;

public class DataSourceHandler implements IMessageHandler {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	
	@Override
	public void handleMessage(IMessage message) {
		String st = new String();
		try{
			st = (String)message.getData();
			String[] values = st.split("##");
			System.out.println(st);
			ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
			testBase();
		}catch(Exception e){
			Message m = new Message ("testBase",null,e.getMessage());
			try {
				Bridge.getInstance().sendMessage(m);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private boolean testBase(){
		Message m;
		boolean b=true;
		try {
			ds.getConnection();
			m = new Message ("testBase",null,"Succes");
		} catch (SQLException e) {
			m = new Message ("testBase",null,e.getMessage());
			b=false;
		}
			try {
				Bridge.getInstance().sendMessage(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return b;
	}

	public DriverManagerDataSource getDs() {
		if(testBase()){
			return ds;
		}else{
			return null;
		}
	}
}
