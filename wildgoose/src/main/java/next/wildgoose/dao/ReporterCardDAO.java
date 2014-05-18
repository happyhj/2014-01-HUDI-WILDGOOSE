package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.database.DataSource;
import next.wildgoose.dto.ReporterCard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReporterCardDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReporterCardDAO.class.getName());
	
	public ReporterCard findReporterById (int reporterId) {
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ReporterCard reporterCard = null;
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT author.id as id, author.email as email, author.name as name, press.name as pressName ");
		query.append("from author JOIN press ON author.press_id = press.id WHERE author.id = ?;");
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setInt(1, reporterId);
			rs = psmt.executeQuery();
			
			if (rs.first()) {
				reporterCard = new ReporterCard();			
				reporterCard.setId(rs.getInt("id"));
				reporterCard.setEmail(rs.getString("email"));
				reporterCard.setName(rs.getString("name"));
				reporterCard.setPressName(rs.getString("pressName"));
				// ID로 검색할 때에는 ArticleCard 사용 -> title이 필요하지 않음.
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(), sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		return reporterCard;
	}
	
	public List<ReporterCard> findReportersByType(String type, String searchQuery, int start, int num) {
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		List<ReporterCard> reporterCards = new ArrayList<ReporterCard>();
		ReporterCard reporterCard = null;
		
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
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setString(1, "%" + searchQuery + "%");
			psmt.setInt(2, start);
			psmt.setInt(3, num);
			rs = psmt.executeQuery();
			
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
