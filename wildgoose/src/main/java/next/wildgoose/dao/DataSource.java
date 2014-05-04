package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

// THIS IS SINGLETON PATTERN
public class DataSource {
	private static DataSource dataSource;
	private BasicDataSource bds;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSource.class.getName());
	
	// CALLED BY DatabaseContextListener
	public static void init(String driver, String userName, String userPw, String dbUrl) {
		dataSource = new DataSource(driver, userName, userPw, dbUrl);
	}
	
	// THIS CONSTRUCTOR RUNS ONLY ONCE
	private DataSource(String driver, String userName, String userPw,
			String dbUrl) {
		bds = new BasicDataSource();
		bds.setDriverClassName(driver);
		bds.setUsername(userName);
		bds.setPassword(userPw);
		bds.setUrl(dbUrl);
		
		bds.setMinIdle(3);
		bds.setMaxIdle(6);
		bds.setMaxOpenPreparedStatements(180);
	}
	
	public static DataSource getInstance() {
		if (dataSource == null) {
			LOGGER.error("Database Connectionpool Not Initialized");
		}
		return dataSource;
	}

	// static getConnection makes code thread-unsafe
	public Connection getConnection() throws SQLException {
		return this.bds.getConnection();
	}
}
