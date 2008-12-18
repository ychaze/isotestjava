package handler;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.DataSourceFactory;

import java.util.List;

import merapi.Bridge;
import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;

public class SQLRequestHandler implements IMessageHandler {
	private final Log logger = LogFactory.getLog(SQLRequestHandler.class);

	public void handleMessage(IMessage message) {
		logger.debug("Handling SQL request...");
		String st = (String)message.getData();
		String[] values = st.split("##");
		DriverManagerDataSource ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
		String request = values[4];
		
		JdbcTemplate t = new JdbcTemplate(ds);
		try {
			Bridge.getInstance().sendMessage(new Message("sqlInfo",null,"Executing query..."));
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error(e1.getMessage());
		}
		List l = null;
		try {
			l = (List) t.queryForList(request);
			try {
				Bridge.getInstance().sendMessage(new Message("sqlInfo",null,"Operation successful"));
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		} catch (Exception e) {
			try {
				Bridge.getInstance().sendMessage(new Message("sqlInfo",null,"ERROR : "+e.getMessage()));
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		}
		if(l!=null){
		try {
			Bridge.getInstance().sendMessage(new Message ("sqlResult",null, l));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	}
}
