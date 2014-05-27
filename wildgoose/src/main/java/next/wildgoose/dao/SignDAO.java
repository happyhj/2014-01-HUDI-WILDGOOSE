package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.dao.template.InsertJdbcTemplate;
import next.wildgoose.dao.template.SelectJdbcTemplate;
import next.wildgoose.dto.Account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SignDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignDAO.class.getName());
	
	public boolean findEmail (final String email) {
		boolean result = false;
		SelectJdbcTemplate template = new SelectJdbcTemplate() {
			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				if (rs.first()) {
					return true;
				}
				return false;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT count(email) as exist FROM user_account WHERE email = ?");

		result = (Boolean) template.select(query.toString());

		return result;
	}
	
	public Account findAccount (final String email) {
		SelectJdbcTemplate template = new SelectJdbcTemplate() {
			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				Account account = null;
				if (rs.first()) {
					account = new Account(rs.getString("email"), rs.getString("password"));
				}
				return account;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM user_account WHERE email = ?");

		return (Account) template.select(query.toString());
	}

	public boolean joinAccount (final Account account) {

		InsertJdbcTemplate template = new InsertJdbcTemplate () {
			
			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, account.getEmail());
				psmt.setString(2, account.getPassword());
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO user_account (email, password) VALUES (?, ?) ");
		
		return template.insert(query.toString());
	}
}
