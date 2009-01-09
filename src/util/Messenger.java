package util;

import manager.DataSourceManager;
import manager.SQLRequestManager;
import merapi.Bridge;
import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;

public class Messenger implements IMessageHandler {

	private DataSourceManager dsm = new DataSourceManager();
	private SQLRequestManager sqlm = new SQLRequestManager();
	
	// Envoie un message sans devoir détenir un objet
	public static void sendMessage(String type,Object message) throws Exception{
			try {
				//TODO DECOUPAGE EN SEGMENTS ---------------
				


				
				
				// -----------------------------------------
				Bridge.getInstance().sendMessage(new Message(type, message));
			} catch (Exception e) {
				throw(e);
			}
	}
	
	// Prends en charge tous les messages pour les types enregistrés
	public void handleMessage(IMessage message) {
		String type = message.getType();
		System.out.println(type);
		if(type.compareTo("dataSource")==0){
			dsm.process((String)message.getData());
		}
		else if(type.compareTo("dataSource_getDBSqlServer")==0){
			dsm.getDbSqlServer((String)message.getData());
		}
		else if(type.compareTo("dataSource_getDBMySql")==0){
			System.out.println("OK LA ?");
			dsm.getDbMySql((String)message.getData());
		}
		else if(type.compareTo("dataSource_getDBPostgre")==0){
			dsm.getDbPostGre((String)message.getData());
		}
		else if (type.compareTo("sqlRequest_request")==0){
			sqlm.process((String)message.getData());
		}
		else if (type.compareTo("sqlRequest_cancel")==0){
			sqlm.stopThread();
		}
	}
	
	// Enregistre cet objet comme recepteur de message de type "type'
	public void registerHandler (String type){
		Bridge.getInstance().registerMessageHandler(type, this);
	}
}
