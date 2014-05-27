package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.dao.template.InsertJdbcTemplate;
import next.wildgoose.database.DataSource;

public class SearchKeywordDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchKeywordDAO.class.getName());
	
	public boolean addKeywordRecord (final String keyword) {
		
		InsertJdbcTemplate template = new InsertJdbcTemplate () {
			
			@Override
			public void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, keyword);
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO search_keyword (keyword) VALUES (?) ");
		query.append("ON DUPLICATE KEY UPDATE count = count + 1");
		
		return template.insert(query.toString());
	}
}	
