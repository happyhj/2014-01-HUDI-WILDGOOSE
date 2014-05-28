package next.wildgoose.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.dao.template.SelectJdbcTemplate;
import next.wildgoose.dto.Reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReporterDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterDAO.class.getName());
	
	public Reporter findReporterById (final int reporterId) {
		
		SelectJdbcTemplate template = new SelectJdbcTemplate() {

			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				Reporter reporter = new Reporter();
				if (rs.first()) {
					reporter.setId(rs.getInt("id"));
					reporter.setEmail(rs.getString("email"));
					reporter.setName(rs.getString("name"));
					reporter.setPressName(rs.getString("pressName"));
					// ID로 검색할 때에는 ArticleCard 사용 -> title이 필요하지 않음.
				}
				return reporter;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setInt(1, reporterId);
			}
		};
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT author.id as id, author.email as email, author.name as name, press.name as pressName ");
		query.append("from author JOIN press ON author.press_id = press.id WHERE author.id = ?;");

		Reporter reporter = (Reporter) template.select(query.toString());

		return reporter;
	}
	
	public List<Reporter> findReportersByType(String type, final String searchQuery, final int start, final int num) {

		SelectJdbcTemplate template = new SelectJdbcTemplate() {

			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				List<Reporter> reporters = new ArrayList<Reporter>();
				Reporter Reporter = null;
				while (rs.next()) {
					Reporter = new Reporter();
					Reporter.setId(rs.getInt("id"));
					Reporter.setEmail(rs.getString("email"));
					Reporter.setName(rs.getString("name"));
					Reporter.setPressName(rs.getString("press_name"));
					Reporter.setArticleTitle(rs.getString("title"));
					reporters.add(Reporter);
				}
				return reporters;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, "%" + searchQuery + "%");
				psmt.setInt(2, start);
				psmt.setInt(3, num);
			}
		};
		
		String where = null;
		if ("name".equals(type)) {
			where = "author.name";
		}
		else if ("url".equals(type)) {
			where = "aa.article_URL";
		}
		StringBuilder query = new StringBuilder();
		query.append("SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ");
		query.append("FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ");
		query.append("WHERE ").append(where).append(" LIKE ? GROUP BY author.id ORDER BY author.name ");
		query.append("LIMIT ?, ?) as result ");
		query.append("JOIN article ON article.URL = result.article_URL ");
		query.append("JOIN press ON result.press_id = press.id");

		List<Reporter> reporters = (List<Reporter>) template.select(query.toString());

		return reporters;
	}
	
	public List<Reporter> getSimilarNames(final String name, final int count) {
		
		SelectJdbcTemplate template = new SelectJdbcTemplate() {

			@Override
			protected Object mapRow(ResultSet rs) throws SQLException {
				List<Reporter> reporters = new ArrayList<Reporter>();
				Reporter reporter = null;
				while (rs.next()) {
					reporter = new Reporter();
					reporter.setName(rs.getString("name"));
					reporters.add(reporter);
				}
				return reporters;
			}

			@Override
			protected void setValues(PreparedStatement psmt) throws SQLException {
				psmt.setString(1, name + "%");
				psmt.setInt(2, count);
			}
		};

		String query = "SELECT name FROM author WHERE name LIKE ? ORDER BY name LIMIT 0, ? ";
		
		return (List<Reporter>) template.select(query);
	}
}
