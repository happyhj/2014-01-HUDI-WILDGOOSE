package next.wildgoose.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DatabaseConnector {

	private static Connection connection;
	
	public static void connect() {
		connect("jdbc:mysql://10.73.45.134:3306/wildgoose_dev", "viewer", "");
	}
	
	public static void connect(String dbURL, String userName, String userPassword) {
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			connection = DriverManager.getConnection(dbURL, userName, userPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static ResultSet select(String query) {
		ResultSet rs = null;
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
