package server;

import handler.DataSourceHandler;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.logging.Logger;
import merapi.Bridge;
public class Server {

	public static void main(String[] args) {
		Logger.getAnonymousLogger().info("Starting Java Server for PeperBoy...");
		Logger.getAnonymousLogger().info("Registrering a new data source handler");
		for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements();){
		    Driver driver = (Driver)e.nextElement();
		    int majorVersion = driver.getMajorVersion();
		    int minorVersion = driver.getMinorVersion();
		    System.out.println("Driver = "+driver.getClass()+
		        " v"+majorVersion+"."+minorVersion);
		}
		Bridge.getInstance().registerMessageHandler("dataSource", new DataSourceHandler());
	}

}
