package manager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.ApplicationConstants;
import util.DataSourceFactory;
import util.Messenger;
import util.SqlRequestThread;

public class SQLRequestManager  {
	
	private final Log logger = LogFactory.getLog(SQLRequestManager.class);
	private SqlRequestThread sqlreqTH ;
	
	public void process(String data) {
			// HANDLE SQL REQUEST ----------
			logger.debug("Handling SQL request...");
			String[] values = data.split("##");
			System.out.println(data);
			DriverManagerDataSource ds = DataSourceFactory.createDS(values[3],
					values[0], values[1], Integer.decode(values[2]));
			String request = values[4];
	
			JdbcTemplate t = new JdbcTemplate(ds);
			try {
				Messenger.sendMessage(ApplicationConstants.SQL_INFO, "Executing query...");
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
	@SuppressWarnings("deprecation")
	public void stopThread(){
			if(sqlreqTH != null){
				// The only way i found to be sure to kill the process
				// I know it is the best solution ever...&
				sqlreqTH.stop();
				logger.debug("STOPPING THREAD : "+sqlreqTH.isInterrupted());
				sqlreqTH=null;
			}
			try {
				Messenger.sendMessage(ApplicationConstants.SQL_INFO, "Query canceled !");
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
	}
}
