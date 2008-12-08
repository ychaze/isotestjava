package util;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceFactory {
	private static DriverManagerDataSource ds = new DriverManagerDataSource();
	
	public static DriverManagerDataSource createDS(String url, String user, String pass, int type){
		switch(type){
		// Oracle
		case 0:
			ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
			break;
		// SQL SERVER
		case 2:
			ds.setDriverClassName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
			break;
		// MYSQL
		case 3:
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			break;
		}
		ds.setPassword(pass);
		ds.setUsername(user);
		ds.setUrl(url);
		
		return ds;	
	}
}
