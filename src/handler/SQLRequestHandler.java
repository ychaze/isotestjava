package handler;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.DataSourceFactory;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;

public class SQLRequestHandler implements IMessageHandler {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	
	@Override
	public void handleMessage(IMessage arg0) {

	}

	public void setDs(DriverManagerDataSource ds) {
		this.ds = ds;
	}

}
