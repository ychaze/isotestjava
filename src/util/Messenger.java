package util;

import manager.DataSourceManager;
import manager.ExcelManager;
import manager.SQLRequestManager;
import merapi.Bridge;
import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;

public class Messenger implements IMessageHandler {

	private DataSourceManager dsm = new DataSourceManager();
	private SQLRequestManager sqlm = new SQLRequestManager();
	private ExcelManager xlm = new ExcelManager();
	// Envoie un message sans devoir détenir un objet
	public static void sendMessage(String type,Object message) throws Exception{
			try {
				Bridge.getInstance().sendMessage(new Message(type, message));
			} catch (Exception e) {
				throw(e);
			}
	}
	
	// Prends en charge tous les messages pour les types enregistrés
	public void handleMessage(IMessage message) {
		String type = message.getType();
		System.out.println(type);
		if(type.compareTo(ApplicationConstants.DATA_SOURCE)==0){
			dsm.process((String)((Message) message).getData());
		}
		else if(type.compareTo(ApplicationConstants.GET_DB_SQLSERVER)==0){
			dsm.getDbSqlServer((String)((Message) message).getData());
		}
		else if(type.compareTo(ApplicationConstants.GET_DB_MYSQL)==0){
			dsm.getDbMySql((String)((Message) message).getData());
		}
		else if(type.compareTo(ApplicationConstants.GET_DB_POSTGRESQL)==0){
			dsm.getDbPostGre((String)((Message) message).getData());
		}
		else if (type.compareTo(ApplicationConstants.REQUEST)==0){
			sqlm.process((String)((Message) message).getData());
		}
		else if (type.compareTo(ApplicationConstants.CANCEL_QUERY)==0){
			sqlm.stopThread();
		}
		else if (type.compareTo(ApplicationConstants.GET_NAME_SHEETS)==0){
			xlm.getNbSheets((String)((Message) message).getData());
		}
		else if(type.compareTo(ApplicationConstants.GET_EXCEL_DATA)==0){
			xlm.process((String)((Message) message).getData());
		}
	}
	
	// Enregistre cet objet comme recepteur de message de type "type'
	public void registerHandler (String type){
		Bridge.getInstance().registerMessageHandler(type, this);
	}
}
