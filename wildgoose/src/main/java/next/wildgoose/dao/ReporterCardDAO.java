package next.wildgoose.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import next.wildgoose.model.DatabaseConnector;
import next.wildgoose.model.ReporterCardData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;


public class ReporterCardDAO {
	
	static Logger logger = LoggerFactory.getLogger(ReporterCardDAO.class.getName());
	
	String mysqlQuery = null;
	ResultSet rs = null;
	List<ReporterCardData> reporterCards = null;
	ReporterCardData reporterCard = null;
	
	
	public List<ReporterCardData> findReportersByName (String name) {
		// Actual logic goes here.
		reporterCards = new ArrayList<ReporterCardData>();
		
		// getting database connection to MySQL server
		// 이름으로 검색하기
		mysqlQuery = "SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ";
		mysqlQuery += "FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ";
		mysqlQuery += "WHERE author.name LIKE '%" + name + "%' GROUP BY author.id ORDER BY author.name) as result ";
		mysqlQuery += "JOIN article ON article.URL = result.article_URL ";
		mysqlQuery += "JOIN press ON result.press_id = press.id";
		//// 이메일이 존재하는 기사 중 검색어가 title content section URL 에 포함되는 기사를 
		//// 기자당 1개씩 뽑아 JOIN한 결과를 얻는다
		
//		mysqlQuery = "SELECT * FROM(SELECT * FROM(";
//		mysqlQuery += "SELECT * FROM wildgoose.article WHERE title LIKE '%" + searchQuery + "%' UNION ";
//		mysqlQuery += "SELECT * FROM wildgoose.article WHERE content LIKE '%" + searchQuery + "%' UNION ";
//		mysqlQuery += "SELECT * FROM wildgoose.article WHERE url LIKE '%" + searchQuery + "%' UNION ";
//		mysqlQuery += "SELECT * FROM wildgoose.article WHERE section LIKE '%" + searchQuery + "%') ";
//		mysqlQuery += "AS article INNER JOIN wildgoose.article_author AS article_author ";
//		mysqlQuery += "ON article.URL = article_author.article_URL) AS result INNER JOIN wildgoose.press AS press ";
//		mysqlQuery += "ON result.press_id = press.id GROUP BY email ORDER BY email";
		
		try {
			rs = DatabaseConnector.select(mysqlQuery);
			
			while (rs.next()) {
				reporterCard = new ReporterCardData();
				reporterCard.setId(rs.getInt("id"));
				reporterCard.setEmail(rs.getString("email"));
				reporterCard.setName(rs.getString("name"));
				reporterCard.setPressName(rs.getString("press_name"));
				reporterCard.setArticleTitle(rs.getString("title"));
				reporterCards.add(reporterCard);
			}
		} catch (SQLException sqle) {
			
			logger.debug(sqle.getMessage(),sqle);	
		}
		
		return reporterCards;
	}
}
