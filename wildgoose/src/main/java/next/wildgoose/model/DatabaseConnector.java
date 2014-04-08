package next.wildgoose.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
	
	public static ResultSet select(String query) throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		return rs;
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
