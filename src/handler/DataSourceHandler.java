package handler;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import server.Server;
import util.DataSourceFactory;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;
import merapi.Bridge;;

public class DataSourceHandler implements IMessageHandler {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	private  Server srv;
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
				synchronized(srv){
					srv.notify();
				}
		} catch (SQLException e) {
			m = new Message ("testBase",null,e.getMessage());
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

	public void setSrv(Server srv) {
		this.srv = srv;
	}

	public Server getSrv() {
		return srv;
	}

}
