package manager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.ApplicationConstants;
import util.DataSourceFactory;
import util.Messenger;

public class DataSourceManager {
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	private final Log logger = LogFactory.getLog(DataSourceManager.class);
	
	
	public void process( String data) {
		logger.debug("Handling DataSource infos...");
		System.out.println(data);
		try{
			String[] values = data.split("##");
			// SPLIT INFOS
			ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
			// GET DB TYPE 
				Messenger.sendMessage("testBase", "Trying to connect to the DB...");
				if(testBase()){
					Messenger.sendMessage(ApplicationConstants.TEST_BASE, "Succes");
				}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void getDbSqlServer(String data){
		String[] values = data.split("##");
		System.out.println(data);
		// SPLIT INFOS
		ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
		JdbcTemplate t = new JdbcTemplate(ds);
		if (testBase()){
			List l = null;
			try{
				l = t.queryForList("SELECT NAME FROM SYS.DATABASES");
			}catch(Exception e){
				logger.error(e.getMessage());
				try {
					Messenger.sendMessage(ApplicationConstants.TEST_BASE,e.getMessage());
				} catch (Exception e2) {
					e2.printStackTrace();
					logger.error(e2.getMessage());
				}
			}
			try {
				Messenger.sendMessage(ApplicationConstants.DB_SQL_SERVER, l);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getDbMySql(String data){
		String[] values = data.split("##");
		System.out.println(data);
		// SPLIT INFOS
		ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
		JdbcTemplate t = new JdbcTemplate(ds);
		if (testBase()){
			List l = null;
			try{
				l = t.queryForList("show databases");
			}catch(Exception e){
				logger.error(e.getMessage());
				try {
					Messenger.sendMessage(ApplicationConstants.TEST_BASE,e.getMessage());
				} catch (Exception e2) {
					e2.printStackTrace();
					logger.error(e2.getMessage());
				}
			}
			try {
				Messenger.sendMessage(ApplicationConstants.DB_MYSQL, l);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void getDbPostGre(String data){
		String[] values = data.split("##");
		// SPLIT INFOS
		ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
		JdbcTemplate t = new JdbcTemplate(ds);
		if (testBase()){
			List l = null;
			try{
				l = t.queryForList("select datname from pg_database");
			}catch(Exception e){
				logger.error(e.getMessage());
				try {
					Messenger.sendMessage(ApplicationConstants.TEST_BASE,e.getMessage());
				} catch (Exception e2) {
					e2.printStackTrace();
					logger.error(e2.getMessage());
				}
			}
			try {
				Messenger.sendMessage(ApplicationConstants.DB_POSTGRESQL, l);
			} catch (Exception e) {
				e.printStackTrace();
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
				Messenger.sendMessage(ApplicationConstants.TEST_BASE,"ERROR : "+e.getMessage());
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
