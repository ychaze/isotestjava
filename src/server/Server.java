package server;

import handler.DataSourceHandler;
import java.util.logging.Logger;
import merapi.Bridge;
public class Server {

	public static void main(String[] args) {
		Logger.getAnonymousLogger().info("Starting Java Server for PeperBoy...");
		Logger.getAnonymousLogger().info("Registrering a new data source handler");
		Bridge.getInstance().registerMessageHandler("dataSource", new DataSourceHandler());
	}

}
