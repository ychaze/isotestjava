package handler;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.DataSourceFactory;
import util.Messenger;

public class DataSourceManager {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	private final Log logger = LogFactory.getLog(DataSourceManager.class);
	
	
	public void process(String type, String sourceType, String data) {
		logger.debug("Handling DataSource infos...");
		try{
			System.out.println(data);
			String[] values = data.split("##");
			// SPLIT INFOS
			ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
			// GET DB TYPE 
			if(sourceType != null){
				JdbcTemplate t = new JdbcTemplate(ds);
				// SQL SERVER: GET DBs
				if(sourceType.compareTo("getDBSqlServer")==0){
					if (testBase()){
						List l = null;
						l = t.queryForList("SELECT NAME FROM SYS.DATABASES");
						Messenger.sendMessage("DBSqlServer", null, l);
					}
				}
				// MYSQL: GET DBs
				else if(sourceType.compareTo("getDBMySql")==0){
					if (testBase()){
						List l = null;
						l = t.queryForList("show databases");
						Messenger.sendMessage("DBMySql", null, l);
					}
				}
				// POSTGRE: GET DBs
				else if(sourceType.compareTo("getDBPostgre")==0){
					if (testBase()){
						List l = null;
						l = t.queryForList("select datname from pg_database");
						Messenger.sendMessage("DBPostgreSql", null, l);
					}
				}
			}
			else{
				Messenger.sendMessage("testBase", null, "Trying to connect to the DB...");
				if(testBase()){
					Messenger.sendMessage("testBase", null, "Succes");
				}
			}
			
		}catch(Exception e){
			logger.error(e.getMessage());
			try {
				Messenger.sendMessage("testBase",null,e.getMessage());
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error(e2.getMessage());
			}
		}
	}
	
	private boolean testBase(){
		boolean b=true;
		logger.info("Testing DB connection...");
		try {
			ds.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			try {
				Messenger.sendMessage("testBase",null,"ERROR : "+e.getMessage());
				Logger.getAnonymousLogger().info(e.getMessage().toString());
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error(e2.getMessage());
			}
			b=false;
		}
			return b;
	}

	public DriverManagerDataSource getDs() {
			return ds;
	}
}
