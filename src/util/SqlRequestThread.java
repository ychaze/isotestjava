package util;

import handler.SQLRequestHandler;

import java.util.List;

import merapi.Bridge;
import merapi.messages.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlRequestThread extends Thread {
	
	private String request;
	private JdbcTemplate t;
	private final Log logger = LogFactory.getLog(SqlRequestThread.class);
	
	public SqlRequestThread (String request, JdbcTemplate t){
		super();
		this.t=t;
		this.request=request;
	}
	public  void stopRequest() throws InterruptedException{
		throw new InterruptedException();
	}
	
	public void run(){
		List l = null;
		// QUERY -----------------------
		try {
			l = (List) t.queryForList(request);
			try {
				Bridge.getInstance().sendMessage(
						new Message("sqlInfo", null, "Operation successful"));
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		} catch (Exception e) {
			try {
				Bridge.getInstance().sendMessage(
						new Message("sqlInfo", null, "ERROR : "
								+ e.getMessage()));
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
		}
		// -------------------------
		// SEND LIST ---------------
		if (l != null) {
			try {
				Bridge.getInstance().sendMessage(
						new Message("sqlResult", null, l));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
		// -------------------------
	}
}
