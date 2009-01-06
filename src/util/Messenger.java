package util;

import handler.DataSourceManager;
import handler.SQLRequestManager;
import merapi.Bridge;
import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;

public class Messenger implements IMessageHandler {

	private DataSourceManager dsm = new DataSourceManager();
	private SQLRequestManager sqlm = new SQLRequestManager();
	
	// Envoie un message sans devoir détenir un objet
	public static void sendMessage(String type, String sourceType,Object message) throws Exception{
			try {
				Bridge.getInstance().sendMessage(new Message(type, sourceType, message));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw(e);
			}
	}
	
	// Prends en charge tous les messages pour les types enregistrés
	public void handleMessage(IMessage message) {
		String type = message.getType();
		if(type.compareTo("dataSource")==0){
			dsm.process(type, message.getSourceType(), (String)message.getData());
		}
		else if (type.compareTo("sqlRequest")==0){
			sqlm.process(type, message.getSourceType(), (String)message.getData());
		}
	}
	
	// Enregistre cet objet comme recepteur de message de type "type'
	public void registerHandler (String type){
		Bridge.getInstance().registerMessageHandler(type, this);
	}
}
