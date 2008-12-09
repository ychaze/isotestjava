package handler;
import java.sql.SQLException;
import java.util.logging.Logger;

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
			ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
			Bridge.getInstance().sendMessage(new Message("testBase",null,"Trying to connect the DB..."));
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
				synchronized(this){
					notify();
				}
		} catch (SQLException e) {
			m = new Message ("testBase",null,"ERROR : "+e.getMessage());
			b=false;
		}
			try {
				Bridge.getInstance().sendMessage(m);
				Logger.getAnonymousLogger().info(m.getData().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return b;
	}

	public DriverManagerDataSource getDs() {
			return ds;
	}
}
