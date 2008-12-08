package server;

import handler.DataSourceHandler;
import handler.SQLRequestHandler;

import java.util.logging.Logger;
import merapi.Bridge;
public class Server {

	public static void main(String[] args) {
		Logger.getAnonymousLogger().info("Starting Java Server for PeperBoy...");
		Logger.getAnonymousLogger().info("Registrering a new data source handler");
		DataSourceHandler dsh = new DataSourceHandler();
		SQLRequestHandler sqlrh = new SQLRequestHandler();
		Bridge.getInstance().registerMessageHandler("dataSource",dsh);
		Bridge.getInstance().registerMessageHandler("sqlRequest", sqlrh);
		sqlrh.setDs(dsh.getDs());
	}
}
