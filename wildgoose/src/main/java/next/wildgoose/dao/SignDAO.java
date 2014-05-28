package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.dao.template.InsertJdbcTemplate;
import next.wildgoose.dao.template.SelectJdbcTemplate;

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
	
	public String findAccount (final String email) {
		// 해당 이메일에 대응되는 password의 존재 유무로 Account 존재 유무를 판단
		SelectJdbcTemplate template = new SelectJdbcTemplate() {
			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				String result = null;
				if (rs.first()) {
					result = rs.getString("password");
				}
				return result;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM user_account WHERE email = ?");

		return (String) template.select(query.toString());
	}

	public boolean joinAccount (final String email, final String password) {

		InsertJdbcTemplate template = new InsertJdbcTemplate () {
			
			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, email);
				psmt.setString(2, password);
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO user_account (email, password) VALUES (?, ?) ");
		
		return template.insert(query.toString());
	}
}
