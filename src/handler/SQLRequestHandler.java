package handler;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.DataSourceFactory;
import util.SqlRequestThread;

import merapi.Bridge;
import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;

public class SQLRequestHandler implements IMessageHandler {
	
	private final Log logger = LogFactory.getLog(SQLRequestHandler.class);
	private SqlRequestThread sqlreqTH ;
	
	public void handleMessage(IMessage message) {
		System.out.println(message.getSourceType());
		if(message.getSourceType().compareTo("request")==0 ){
			// HANDLE SQL REQUEST ----------
			logger.debug("Handling SQL request...");
			String st = (String) message.getData();
			String[] values = st.split("##");
			DriverManagerDataSource ds = DataSourceFactory.createDS(values[3],
					values[0], values[1], Integer.decode(values[2]));
			String request = values[4];
	
			JdbcTemplate t = new JdbcTemplate(ds);
			try {
				Bridge.getInstance().sendMessage(
						new Message("sqlInfo", null, "Executing query..."));
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
			// -----------------------------
			// USE THE THREAD 
			
			sqlreqTH = new SqlRequestThread(request,t);
			sqlreqTH.start();
			logger.debug("Starting thread for query : "+request);
			// ----------------------------
		}
		else{
			sqlreqTH.stop();
			logger.debug("STOPPING THREAD : "+sqlreqTH.isInterrupted());
			sqlreqTH=null;
			try {
				Bridge.getInstance().sendMessage(
						new Message("sqlInfo", null, "Query canceled !"));
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		}
	}
}
