package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.database.DataSource;
import next.wildgoose.dto.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SignDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignDAO.class.getName());
	
	public boolean findEmail (String email) {
		boolean result = false;
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		int len = 0;
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT count(email) as exist FROM user_account WHERE email = ?");
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, email);
			rs = psmt.executeQuery();
			if (rs.next()) {
				len = rs.getInt("exist");
				if (len == 0) {
					result = true;
				}
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		
		return result;
	}
	
	public Account findAccount (String email) {
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		Account account = null;
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM user_account WHERE email = ?");
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, email);
			rs = psmt.executeQuery();
			if (rs.next()) {
				account = new Account(rs.getString("email"), rs.getString("password"));
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		
		return account;
	}

	public boolean joinAccount (Account account) {
		boolean result = false;
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO user_account (email, password) VALUES (?, ?) ");
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, account.getEmail());
			psmt.setString(2, account.getPassword());
			psmt.execute();
			result = true;
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		return result;
	}
	
	
}
