package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.dao.template.JdbcTemplate;
import next.wildgoose.dao.template.PreparedStatementSetter;
import next.wildgoose.dao.template.RowMapper;
import next.wildgoose.dto.Account;


public class SignDAO {
		
	public boolean findEmail (final String email) {		
		JdbcTemplate t = new JdbcTemplate();
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
				
			}
			
		};
		
		RowMapper rm = new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				if (rs.first()) {
					return true;
				}
				return false;
			}
			
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT count(email) as exist FROM user_account WHERE email = ?");
		
		return (Boolean) t.execute(query.toString(), pss, rm);
	}
	
	public Account findAccount (final String email) {
		JdbcTemplate t = new JdbcTemplate();
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
				
			}
			
		};
		
		RowMapper rm = new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs) throws SQLException {
				Account account = null;
				if (rs.first()) {
					account = new Account(rs.getString("email"), rs.getString("password"));
				}
				return account;
			}
			
		};
		
		
		String query = "SELECT * FROM user_account WHERE email = ?";

		return (Account) t.execute(query, pss, rm);
	}

	public boolean joinAccount (final Account account) {
		JdbcTemplate t = new JdbcTemplate();
		PreparedStatementSetter pss = new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, account.getEmail());
				psmt.setString(2, account.getPassword());
				
			}
			
		};

		String query = "INSERT INTO user_account (email, password) VALUES (?, ?) ";
		
		return (Boolean) t.execute(query, pss);
	}
}
