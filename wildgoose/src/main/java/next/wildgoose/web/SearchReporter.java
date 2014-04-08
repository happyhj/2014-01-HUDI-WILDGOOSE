package next.wildgoose.web;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.model.DatabaseConnector;
import next.wildgoose.model.Reporter;
import next.wildgoose.model.VerifySearchQuery;
import next.wildgoose.model.WebError;
// DB 연결을 위한 라이브러리

//HTML entity로 encoding된 한글을 다루기 위한 라이브러
//import org.apache.commons.lang.StringEscapeUtils;

public class SearchReporter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchReporter() {
        super();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchQuery = null;
		WebError webError = null;
		
		if (request.getParameter("q") != null && request.getParameter("q") != "" ) {
			searchQuery = new String(request.getParameter("q").getBytes("8859_1"), "UTF-8");
			VerifySearchQuery verifySQ = new VerifySearchQuery(searchQuery);
			webError = verifySQ.getError();
		} else {
			System.out.println("입력이 없다");
			searchQuery = null;
		}
		
		System.out.println(searchQuery);
		
		// Actual logic goes here.
		ArrayList<Reporter> reporters = new ArrayList<Reporter>();
		
		// getting database connection to MySQL server
		try {
			if (webError != null) {
				request.setAttribute("webError", webError);
			}
			else if (searchQuery != null) {
				String mysqlQuery = null;
				ResultSet rs = null;
				
				//// 이메일이 존재하는 기사 중 검색어가 title content section URL 에 포함되는 기사를 
				//// 기자당 1개씩 뽑아 JOIN한 결과를 얻는다
				mysqlQuery = "SELECT * FROM(SELECT * FROM(";
				mysqlQuery += "SELECT * FROM wildgoose.article WHERE title LIKE '%" + searchQuery + "%' UNION ";
				mysqlQuery += "SELECT * FROM wildgoose.article WHERE content LIKE '%" + searchQuery + "%' UNION ";
				mysqlQuery += "SELECT * FROM wildgoose.article WHERE url LIKE '%" + searchQuery + "%' UNION ";
				mysqlQuery += "SELECT * FROM wildgoose.article WHERE section LIKE '%" + searchQuery + "%') ";
				mysqlQuery += "AS article INNER JOIN wildgoose.article_author AS article_author ";
				mysqlQuery += "ON article.URL = article_author.article_URL) AS result INNER JOIN wildgoose.press AS press ";
				mysqlQuery += "ON result.press_id = press.id GROUP BY email ORDER BY email";
				
						
				rs = DatabaseConnector.select(mysqlQuery);
				
				while (rs.next()) {
					Reporter reporter = new Reporter();
					reporter.setEmail(rs.getString("email"));
					reporter.setAuthorInfo(rs.getString("author_info"));
					reporter.setArticleURL(rs.getString("article_URL"));
					reporter.setArticleTitle(rs.getString("title"));
					reporter.setPressName(rs.getString("name"));				
					reporters.add(reporter);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("reporters", reporters);
			request.setAttribute("searchQuery", searchQuery);
			request.getRequestDispatcher("SearchReporter.jsp").forward(request, response);
		}	
	}
}
