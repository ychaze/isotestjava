package handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.DataSourceFactory;
import util.Messenger;
import util.SqlRequestThread;

public class SQLRequestManager  {
	
	private final Log logger = LogFactory.getLog(SQLRequestManager.class);
	private SqlRequestThread sqlreqTH ;
	
	public void process(String type, String sourceType, String data) {
		if(sourceType.compareTo("request")==0 ){
			// HANDLE SQL REQUEST ----------
			logger.debug("Handling SQL request...");
			String[] values = data.split("##");
			DriverManagerDataSource ds = DataSourceFactory.createDS(values[3],
					values[0], values[1], Integer.decode(values[2]));
			String request = values[4];
	
			JdbcTemplate t = new JdbcTemplate(ds);
			try {
				Messenger.sendMessage("sqlInfo", null, "Executing query...");
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
			if(sqlreqTH != null){
				sqlreqTH.stop();
				logger.debug("STOPPING THREAD : "+sqlreqTH.isInterrupted());
				sqlreqTH=null;
			}
			try {
				Messenger.sendMessage("sqlInfo", null, "Query canceled !");
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		}
	}
}
