package server;

import handler.DataSourceHandler;
import handler.SQLRequestHandler;

import java.util.logging.Logger;
import merapi.Bridge;
public class Server {
	private DataSourceHandler dsh = new DataSourceHandler();
	private SQLRequestHandler sqlrh = new SQLRequestHandler();
	
	public Server(){
		Logger.getAnonymousLogger().info("Starting Java Server for PeperBoy...");
		Logger.getAnonymousLogger().info("Registrering a new data source handler");
		Bridge.getInstance().registerMessageHandler("dataSource",dsh);
		Bridge.getInstance().registerMessageHandler("sqlRequest", sqlrh);
		waitForDS();

	}
	private void waitForDS(){
		try {
			synchronized(dsh){
				dsh.wait();
			}
			Logger.getAnonymousLogger().info(dsh.getDs().toString());
			sqlrh.setDs(dsh.getDs());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		new Server();
	}
}
