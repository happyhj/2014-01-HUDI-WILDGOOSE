package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.model.DataSource;
import next.wildgoose.model.ReporterCard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReporterCardDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterCardDAO.class.getName());
	

	public ReporterCard findReporterById (int reporterId) throws SQLException {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ReporterCard reporterCard = null;
		
		try {
			// getting database connection to MySQL server
			conn = DataSource.getInstance().getConnection();
			
			StringBuilder query = new StringBuilder();
			query.append("SELECT author.id as id, author.email as email, author.name as name, press.name as pressName ");
			query.append("from author JOIN press ON author.press_id = press.id WHERE author.id = ?;");
			
			psmt = conn.prepareStatement(query.toString());
			psmt.setInt(1, reporterId);
			
			// sql에 query 요청
			rs = psmt.executeQuery();
			
			reporterCard = new ReporterCard();			
			if (rs.first()) {
				reporterCard.setId(rs.getInt("id"));
				reporterCard.setEmail(rs.getString("email"));
				reporterCard.setName(rs.getString("name"));
				reporterCard.setPressName(rs.getString("pressName"));
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(),sqle);
		} finally {
			if (rs != null) rs.close();
			if (psmt != null) psmt.close();
			if (conn != null) conn.close();
		}
		
		
		return reporterCard;
	}
	
	
	public List<ReporterCard> findReportersByName (String name) throws SQLException {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<ReporterCard> reporterCards = null;
		ReporterCard reporterCard = null;

		/*
		 * 이메일이 존재하는 기사 중 검색어가 title content section URL 에 포함되는 기사를 
		 * 기자당 1개씩 뽑아 JOIN한 결과를 얻는다
		mysqlQuery = "SELECT * FROM(SELECT * FROM(";
		mysqlQuery += "SELECT * FROM wildgoose.article WHERE title LIKE '%" + searchQuery + "%' UNION ";
		mysqlQuery += "SELECT * FROM wildgoose.article WHERE content LIKE '%" + searchQuery + "%' UNION ";
		mysqlQuery += "SELECT * FROM wildgoose.article WHERE url LIKE '%" + searchQuery + "%' UNION ";
		mysqlQuery += "SELECT * FROM wildgoose.article WHERE section LIKE '%" + searchQuery + "%') ";
		mysqlQuery += "AS article INNER JOIN wildgoose.article_author AS article_author ";
		mysqlQuery += "ON article.URL = article_author.article_URL) AS result INNER JOIN wildgoose.press AS press ";
		mysqlQuery += "ON result.press_id = press.id GROUP BY email ORDER BY email";
		*/

		// Actual logic goes here.
		try {
			conn = DataSource.getInstance().getConnection();
			
			StringBuilder query = new StringBuilder();
			// 이름으로 검색하기
			query.append("SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ");
			query.append("FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ");
			query.append("WHERE author.name LIKE ? GROUP BY author.id ORDER BY author.name) as result ");
			query.append("JOIN article ON article.URL = result.article_URL ");
			query.append("JOIN press ON result.press_id = press.id");
			
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, "%" + name + "%");
			
			// sql에 query 요청
			rs = psmt.executeQuery();
			
			reporterCards = new ArrayList<ReporterCard>();
			while (rs.next()) {
				reporterCard = new ReporterCard();
				reporterCard.setId(rs.getInt("id"));
				reporterCard.setEmail(rs.getString("email"));
				reporterCard.setName(rs.getString("name"));
				reporterCard.setPressName(rs.getString("press_name"));
				reporterCard.setArticleTitle(rs.getString("title"));
				reporterCards.add(reporterCard);
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(),sqle);
			reporterCards = null;
		} finally {
			if (rs != null) rs.close();
			if (psmt != null) psmt.close();
			if (conn != null) conn.close();
		}
		
		return reporterCards;
	}
}