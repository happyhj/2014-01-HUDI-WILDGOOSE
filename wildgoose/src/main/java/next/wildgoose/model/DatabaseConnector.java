package next.wildgoose.model;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DatabaseConnector {
	
	static Logger logger = LoggerFactory.getLogger(DatabaseConnector.class.getName());
	private static Connection connection;
	
	public static void connect() {
		connect("jdbc:mysql://10.73.45.134:3306/wildgoose_dev", "viewer", "");
	}
	
	public static Connection getConnection() {
		
		connect();
		
		return connection;
	}
	
	public static void connect(String dbURL, String userName, String userPassword) {
		try {
			connection = DataSource.getInstance().getConnection();
//			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//			connection = DriverManager.getConnection(dbURL, userName, userPassword);
		} catch (SQLException sqle) {
			logger.debug(sqle.getMessage(),sqle);	
		} catch (IOException e) {
			logger.debug(e.getMessage(),e);	
		} catch (PropertyVetoException e) {
			logger.debug(e.getMessage(),e);	
		}
	}
	
	public static ResultSet select(String query) {
		ResultSet rs = null;
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException sqle) {
			logger.debug(sqle.getMessage(),sqle);	
		}
		return rs;
	}
	
	public static void close() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException sqle) {
			logger.debug(sqle.getMessage(),sqle);	
		}
	}
}
