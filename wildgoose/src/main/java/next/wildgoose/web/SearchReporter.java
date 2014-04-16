package next.wildgoose.web;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.exception.SearchQueryIllegalArgumentException;
import next.wildgoose.exception.SearchQueryNullPointerException;
import next.wildgoose.exception.SearchQueryUnsupportedOperationException;
import next.wildgoose.model.DatabaseConnector;
import next.wildgoose.model.ReporterCardData;
import next.wildgoose.model.SearchQuery;
//import next.wildgoose.model.WebError;
import next.wildgoose.utility.Utility;
import next.wildgoose.utility.Wildgoose;


public class SearchReporter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String mysqlQuery = null;
		ResultSet rs = null;
		List<ReporterCardData> reporterCards = null;
		ReporterCardData reporterCard = null;
		RequestDispatcher reqDispatcher = null;
		String dispatchingPage = null;
		SearchQuery searchQuery = null;
		
		Utility utility = new Utility();
		
		try {
			searchQuery = new SearchQuery (request.getParameter("q"), utility, "UTF-8");

			// Actual logic goes here.
			reporterCards = new ArrayList<ReporterCardData>();
			
			// getting database connection to MySQL server
			// 이름으로 검색하기
			mysqlQuery = "SELECT result.id as id, result.name as name, result.email as email, article.title as title, press.name as press_name ";
			mysqlQuery += "FROM (SELECT * FROM author JOIN article_author AS aa ON author.id = aa.author_id ";
			mysqlQuery += "WHERE author.name LIKE '%" + searchQuery + "%' GROUP BY author.id ORDER BY author.name) as result ";
			mysqlQuery += "JOIN article ON article.URL = result.article_URL ";
			mysqlQuery += "JOIN press ON result.press_id = press.id";
			//// 이메일이 존재하는 기사 중 검색어가 title content section URL 에 포함되는 기사를 
			//// 기자당 1개씩 뽑아 JOIN한 결과를 얻는다
//			mysqlQuery = "SELECT * FROM(SELECT * FROM(";
//			mysqlQuery += "SELECT * FROM wildgoose.article WHERE title LIKE '%" + searchQuery + "%' UNION ";
//			mysqlQuery += "SELECT * FROM wildgoose.article WHERE content LIKE '%" + searchQuery + "%' UNION ";
//			mysqlQuery += "SELECT * FROM wildgoose.article WHERE url LIKE '%" + searchQuery + "%' UNION ";
//			mysqlQuery += "SELECT * FROM wildgoose.article WHERE section LIKE '%" + searchQuery + "%') ";
//			mysqlQuery += "AS article INNER JOIN wildgoose.article_author AS article_author ";
//			mysqlQuery += "ON article.URL = article_author.article_URL) AS result INNER JOIN wildgoose.press AS press ";
//			mysqlQuery += "ON result.press_id = press.id GROUP BY email ORDER BY email";
			
					
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
		}
		// query존재하지 않는 경우
		catch (SearchQueryNullPointerException sqnpe) {
			dispatchingPage = Wildgoose.SUCCESS_PAGE;
		}
		
		// query의 encoding이 실패한 경우
		catch (SearchQueryUnsupportedOperationException squoe) {
			dispatchingPage = Wildgoose.ERROR_PAGE;
		}
		
		// query에 에러가 발생한 경우
		catch (SearchQueryIllegalArgumentException sqiae) {
			dispatchingPage = Wildgoose.ERROR_PAGE;
		}
		
		// sql에 에러가 발생한 경우
		catch (SQLException sqle) {
			dispatchingPage = Wildgoose.ERROR_PAGE;
		}
		
		// 예측하지 못한 예외가 발생한 경우
		catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			// exception이 발생하지 않은 경우
			if (dispatchingPage == null) {
				dispatchingPage = Wildgoose.SUCCESS_PAGE;
				request.setAttribute("reporterCards", reporterCards);
				request.setAttribute("searchQuery", searchQuery);
			}
		}
		
		/*
		 * 공백검색, 여백포함검색, 내용없는검색에 대해서 에러는 처리하나
		 * 현재는 페이지가 forword로 전달되어 url는 수정하지 못하는 문제점이 있습니다. 
		 */
		reqDispatcher = request.getRequestDispatcher(dispatchingPage);
		reqDispatcher.forward(request, response);
	}
}
