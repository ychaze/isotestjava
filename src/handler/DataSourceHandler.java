package handler;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import util.DataSourceFactory;

import merapi.messages.IMessage;
import merapi.messages.IMessageHandler;
import merapi.messages.Message;
import merapi.Bridge;;

public class DataSourceHandler implements IMessageHandler {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	@Override
	public void handleMessage(IMessage message) {
		String st = new String();
		try{
			st = (String)message.getData();
			String[] values = st.split("##");
			ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
			if(message.getSourceType() !=null){
				JdbcTemplate t = new JdbcTemplate(ds);
				if(message.getSourceType().compareTo("getDBSqlServer")==0){
					if (testBase()){
						List l = null;
						l = t.queryForList("SELECT NAME FROM SYS.DATABASES");
						Bridge.getInstance().sendMessage(new Message("DBSqlServer",null,l));
					}
				}
				else if(message.getSourceType().compareTo("getDBMySql")==0){
					if (testBase()){
						List l = null;
						l = t.queryForList("show databases");
						Bridge.getInstance().sendMessage(new Message("DBMySql",null,l));
					}
				}
			}
			else{
				Bridge.getInstance().sendMessage(new Message("testBase",null,"Trying to connect to the DB..."));
				if(testBase()){
					Bridge.getInstance().sendMessage(new Message ("testBase",null,"Succes"));
				}
			}
			
		}catch(Exception e){
			Message m = new Message ("testBase",null,e.getMessage());
			try {
				Bridge.getInstance().sendMessage(m);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private boolean testBase(){
		Message m;
		boolean b=true;
		try {
			ds.getConnection();
		} catch (SQLException e) {
			m = new Message ("testBase",null,"ERROR : "+e.getMessage());
			try {
				Bridge.getInstance().sendMessage(m);
				Logger.getAnonymousLogger().info(m.getData().toString());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			b=false;
		}
			return b;
	}

	public DriverManagerDataSource getDs() {
			return ds;
	}
}
