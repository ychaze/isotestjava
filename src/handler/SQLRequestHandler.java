package handler;

import java.util.logging.Logger;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

import merapi.Bridge;
import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;

public class SQLRequestHandler implements IMessageHandler {
	
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	
	@Override
	public void handleMessage(IMessage message) {
		String request = new String();
		request = (String)message.getData();
		Logger.getAnonymousLogger().info(request);
		JdbcTemplate t = new JdbcTemplate(ds);
		try {
			Bridge.getInstance().sendMessage(new Message("sqlInfo",null,"Executing query..."));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		List l = null;
		try {
			l = (List) t.queryForList(request);
			try {
				Bridge.getInstance().sendMessage(new Message("sqlInfo",null,"Operation successful"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				Bridge.getInstance().sendMessage(new Message("sqlInfo",null,"ERROR : "+e.getMessage()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if(l!=null){
		Logger.getAnonymousLogger().info(String.valueOf(l.size()));}
		try {
			Bridge.getInstance().sendMessage(new Message ("sqlResult",null, l));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDs(DriverManagerDataSource ds) {
		this.ds = ds;
	}

}
