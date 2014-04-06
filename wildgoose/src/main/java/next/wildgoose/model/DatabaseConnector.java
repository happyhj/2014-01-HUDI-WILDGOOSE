package next.wildgoose.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnector {

	private static Connection connection;
	
	public static void connect(String dbURL, String userName, String userPassword) {
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			connection = DriverManager.getConnection(dbURL, userName, userPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ResultSet select(String query) {
		return null;
	}
	
	public static void close() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
