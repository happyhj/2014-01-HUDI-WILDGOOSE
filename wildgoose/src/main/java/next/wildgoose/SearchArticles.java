package next.wildgoose;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Enumeration;


// DB 연결을 위한 라이브러리
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// HTML entity로 encoding된 한글을 다루기 위한 라이브러
import org.apache.commons.lang.StringEscapeUtils;

public class SearchArticles extends HttpServlet {

	private static final long serialVersionUID = 1L;
	/**
	 * Default constructor.
	 */
	public SearchArticles() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		String search_query = null;
		
		if(request.getParameter("q")!=null) {
			search_query = new String(request.getParameter("q").getBytes("8859_1"),"UTF-8");
		} else {
			search_query = "";
		}
		
		System.out.println(search_query);
		
		// Actual logic goes here.
		ArrayList<Article> articles = new ArrayList<Article>();
		
		// getting database connection to MySQL server
		try {
			if(request.getParameter("q")!=null) {
				String dbURL = "jdbc:mysql://10.73.45.134:3306/Wildgoose?useUnicode=true&characterEncoding=UTF8";
				String username = "root";
				String password = "wildgoose";
		
				Connection dbCon = null;
				Statement stmt = null;
				ResultSet rs = null;
		
				String mysql_query = "SELECT * FROM Articles WHERE title LIKE '%"+search_query+"%'";
				
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				dbCon = DriverManager.getConnection(dbURL, username, password);
				// getting PreparedStatment to execute query
				stmt = dbCon.prepareStatement(mysql_query);
				// Resultset returned by query
				rs = stmt.executeQuery(mysql_query);
				while (rs.next()) {
					Article article = new Article();
					article.title = StringEscapeUtils.escapeHtml(rs.getString("title"));
//					article.URL = StringEscapeUtils.escapeHtml(rs.getString("URL"));
					article.content = StringEscapeUtils.escapeHtml(rs.getString("content"));
//					article.section = StringEscapeUtils.escapeHtml(rs.getString("section"));
					article.author = StringEscapeUtils.escapeHtml(rs.getString("author"));
					article.datetime = StringEscapeUtils.escapeHtml(rs.getString("datetime"));
					articles.add(article);
				}
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			request.setAttribute("articles", articles);
			request.setAttribute("search_query", search_query);
			request.getRequestDispatcher("SearchArticles.jsp").forward(request, response);
		}
		
	} 
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}