package next.wildgoose.web;

import java.io.IOException;
// DB 연결을 위한 라이브러리
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.model.Reporter;

//HTML entity로 encoding된 한글을 다루기 위한 라이브러
import org.apache.commons.lang.StringEscapeUtils;

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
		String search_query = null;
		
		if(request.getParameter("q")!=null && request.getParameter("q")!="" ) {
			search_query = new String(request.getParameter("q").getBytes("8859_1"),"UTF-8");
		} else {
			System.out.println("입력이 없다");
			search_query = null;
		}
		
		System.out.println(search_query);
		
		// Actual logic goes here.
		ArrayList<Reporter> reporters = new ArrayList<Reporter>();
		
		// getting database connection to MySQL server
		try {
			if(search_query!=null) {
				String dbURL = "jdbc:mysql://10.73.45.134:3306/wildgoose?useUnicode=true&characterEncoding=UTF8";
//				String dbURL = "jdbc:mysql://127.0.0.1:3306/wildgoose?useUnicode=true&characterEncoding=UTF8";

				String username = "root";
				String password = "wildgoose";
//				String password = "kimi5423";
				Connection dbCon = null;
				Statement stmt = null;
				ResultSet rs = null;
		
				//// 이메일이 존재하는 기사 중 검색어가 title content section URL 에 포함되는 기사를 
				//// 기자당 1개씩 뽑아 JOIN한 결과를 얻는다
				String mysql_query = "SELECT * FROM(SELECT * FROM(";
				mysql_query += "SELECT * FROM wildgoose.article WHERE title LIKE '%"+search_query+"%' UNION ";
				mysql_query += "SELECT * FROM wildgoose.article WHERE content LIKE '%"+search_query+"%' UNION ";
				mysql_query += "SELECT * FROM wildgoose.article WHERE url LIKE '%"+search_query+"%' UNION ";
				mysql_query += "SELECT * FROM wildgoose.article WHERE section LIKE '%"+search_query+"%') ";
				mysql_query += "AS article INNER JOIN wildgoose.article_author AS article_author ";
				mysql_query += "ON article.URL = article_author.article_URL) AS result INNER JOIN wildgoose.press AS press ";
				mysql_query += "ON result.press_id = press.id GROUP BY email ORDER BY email";
				
						
				//// article_author 테이블에서 기사의 작성자가 존재한다면 다 
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				dbCon = DriverManager.getConnection(dbURL, username, password);
				stmt = dbCon.prepareStatement(mysql_query);
				rs = stmt.executeQuery(mysql_query);
				while (rs.next()) {
					Reporter reporter = new Reporter();
					reporter.email = StringEscapeUtils.escapeHtml(rs.getString("email"));
					reporter.author_info = StringEscapeUtils.escapeHtml(rs.getString("author_info"));
					reporter.article_URL = StringEscapeUtils.escapeHtml(rs.getString("article_URL"));
					reporter.article_title = StringEscapeUtils.escapeHtml(rs.getString("title"));
					reporter.press_name = StringEscapeUtils.escapeHtml(rs.getString("name"));				
					reporters.add(reporter);
				}
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			request.setAttribute("reporters", reporters);
			request.setAttribute("search_query", search_query);
			request.getRequestDispatcher("SearchReporter.jsp").forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
