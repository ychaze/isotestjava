package server;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Messenger;

public class Server {
	private Messenger messenger = new Messenger();
	private final Log logger = LogFactory.getLog(Server.class);
	public Server(){
		logger.info("Starting Java Server for PaperBoy...");
		logger.info("Registrering a new data source handler");
		messenger.registerHandler("dataSource");
		logger.info("Registrering a new SQL Request handler");
		messenger.registerHandler("sqlRequest_request");
		messenger.registerHandler("dataSource_getDBSqlServer");
		messenger.registerHandler("dataSource_getDBMySql");
		messenger.registerHandler("dataSource_getDBPostgre");
		messenger.registerHandler("sqlRequest_cancel");
	}
	
	public static void main(String[] args) {
		//Start the java server for merapi
		new Server();
		
		//Start the air application
		
		//Get the os name
			/*
		String os = System.getProperty("os.name");
		System.out.println(os);
		Runtime runtime = Runtime.getRuntime();
		Process p;
		try {
			if(os.substring(0,3).compareTo("Win")==0){
				 p = runtime.exec("login");
			}
			else if (os.substring(0,3).compareTo("Mac")==0){
				 p = runtime.exec("login.app/Contents/MacOS/login");
			}
			else{
				 p = runtime.exec("./login");
			}
			try {
				p.waitFor();
				System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			} catch (IOException e) {
				e.printStackTrace();
			}*/
	}
}
