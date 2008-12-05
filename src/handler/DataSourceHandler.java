package handler;
import java.io.ObjectInput;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import util.DataSource;

public class DataSourceHandler implements IMessageHandler {
	private DataSource ds;
	@Override
	public void handleMessage(IMessage message) {
		try{
			ds.readExternal((ObjectInput)message.getData());
			System.out.println(ds.getLogin());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
