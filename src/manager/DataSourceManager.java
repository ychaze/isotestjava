package manager;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import util.ApplicationConstants;
import util.DataSourceFactory;
import util.Messenger;

public class DataSourceManager {
	// DATA SOURCE
	private DriverManagerDataSource ds = new DriverManagerDataSource();
	private final Log logger = LogFactory.getLog(DataSourceManager.class);
	
	/**
	 * Test for the connection param
	 * @param data - Connection params
	 */
	public void process( String data) {
		logger.debug("Handling DataSource infos...");
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
			logger.error(e.getMessage());
		}
	}
	
	public void getDbSqlServer(String data){
		
		// SPLIT INFOS
		String[] values = data.split("##");
		
		// Ask the factory to create the correct datasource for the database supplied
		ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
		
		// Create spring jdbctemplate
		JdbcTemplate t = new JdbcTemplate(ds);
		if (testBase()){
			List<?> l = null;
			try{
				l = t.queryForList("SELECT NAME FROM SYS.DATABASES");
			}catch(Exception e){
				logger.error(e.getMessage());
				// When error send to flex the error
				try {
					Messenger.sendMessage(ApplicationConstants.TEST_BASE,e.getMessage());
				} catch (Exception e2) {
					e2.printStackTrace();
					logger.error(e2.getMessage());
				}
			}
			// Send the list of databases to flex
			try {
				Messenger.sendMessage(ApplicationConstants.DB_SQL_SERVER, l);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Grab the MySQL databases
	 * @param data connection params
	 */
	public void getDbMySql(String data){
		
		// SPLIT INFOS
		String[] values = data.split("##");
		
		ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
		
		JdbcTemplate t = new JdbcTemplate(ds);
		
		if (testBase()){
			List<?> l = null;
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
				logger.error(e.getMessage());
			}
		}
	}
	/**
	 * Grab the PostgreSQL databases
	 * @param data connection params
	 */
	public void getDbPostGre(String data){
		
		// SPLIT INFOS
		String[] values = data.split("##");
		
		ds = DataSourceFactory.createDS(values[3], values[0], values[1], Integer.decode(values[2]));
		
		JdbcTemplate t = new JdbcTemplate(ds);
		
		if (testBase()){
			List<?> l = null;
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
	
	
	/**
	 * Test the connection to the database
	 * @return
	 */
	private boolean testBase(){
		boolean b=true;
		logger.info("Testing DB connection...");
		try {
			ds.getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			try {
				Messenger.sendMessage(ApplicationConstants.TEST_BASE,"ERROR : "+e.getMessage());
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
			b=false;
		}
			return b;
	}
}
