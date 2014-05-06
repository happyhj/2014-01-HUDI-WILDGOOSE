package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.dto.ReporterCard;
import next.wildgoose.pool.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReporterCardDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterCardDAO.class.getName());
	

	public ReporterCard findReporterById (int reporterId) {
		
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
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		
		return reporterCard;
	}
	
	public List<ReporterCard> findReportersByURL(String URL, int start, int end) {
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<ReporterCard> reporterCards = null;
		ReporterCard reporterCard = null;

		// Actual logic goes here.
		try {
			conn = DataSource.getInstance().getConnection();
			
			StringBuilder query = new StringBuilder();
			// 이름으로 검색하기
			query.append("SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ");
			query.append("FROM (");
			query.append("SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ");
			query.append("WHERE aa.article_URL LIKE ? GROUP BY author.id ORDER BY author.name ");
			query.append("LIMIT ?, ?");
			query.append(") as result ");
			query.append("JOIN article ON article.URL = result.article_URL ");
			query.append("JOIN press ON result.press_id = press.id");
			
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, "%" + URL + "%");
			psmt.setInt(2, start);
			psmt.setInt(3, end);
			
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
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		
		return reporterCards;
	}
	
	
	public List<ReporterCard> findReportersByName (String name, int start, int end) {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<ReporterCard> reporterCards = null;
		ReporterCard reporterCard = null;
		String someName = "%" + name + "%";

		// Actual logic goes here.
		try {
			conn = DataSource.getInstance().getConnection();
			
			StringBuilder query = new StringBuilder();
			// 이름으로 검색하기
			query.append("SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ");
			query.append("FROM (");
			query.append("SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ");
			query.append("WHERE author.name LIKE ? GROUP BY author.id ORDER BY author.name ");
			query.append("LIMIT ?, ?");
			query.append(") as result ");
			query.append("JOIN article ON article.URL = result.article_URL ");
			query.append("JOIN press ON result.press_id = press.id");
			
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, someName);
			psmt.setInt(2, start);
			psmt.setInt(3, end);
			LOGGER.debug(psmt.toString());
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
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		
		return reporterCards;
	}
	
}