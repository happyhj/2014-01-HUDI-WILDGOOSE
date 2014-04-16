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

import next.wildgoose.model.ArticleCardData;
import next.wildgoose.model.DatabaseConnector;
import next.wildgoose.model.ReporterCardData;

/**
 * Servlet implementation class ShowReporter
 */
public class ShowReporter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowReporter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/* 
		 * getRequestURI() : /reporters/HBKim1
		 * getRequestURL() : http://10.73.45.145:8080/reporters/HBKim1
		 */
		String uri = request.getRequestURI();
		
		/* 
		 * 한글을 입력 받을 때 URI가 깨지는 문제를 해결하기 위해 사용했습니다
		 * URLDecoder는 deprecated될 예정으로 다른 좋은 방법을 찾아야 하겠습니다
		 */
		/*
		 * 현재로서는 URI가 int형으로 전달되기 때문에 Decoder를 지우고 실행해도 문제가 발생하지 않습니다.
		 * 추후 기자 이름을 ID로 삼아 검색하게 되면 다시 인코딩 문제를 고민해야 합니다.
		 */
		int id = Integer.parseInt(uri.substring("/reporters/".length()));
		
		String getNameQuery = "SELECT author.id as id, author.email as email, author.name as name, press.name as pressName "
				+ "from author JOIN press ON author.press_id = press.id WHERE author.id = " + id + ";";
		ReporterCardData reporterCard = new ReporterCardData();
		ResultSet rs = DatabaseConnector.select(getNameQuery);
		try {
			if (rs.first()) {
				reporterCard.setId(rs.getInt("id"));
				reporterCard.setEmail(rs.getString("email"));
				reporterCard.setName(rs.getString("name"));
				reporterCard.setPressName(rs.getString("pressName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String mysqlQuery = "SELECT article.URL as url, article.title as title, "
				+ "article.section_id as section, article.content as content, article.datetime as datetime "
				+ "FROM article_author JOIN article ON article.URL = article_author.article_URL "
				+ "WHERE article_author.author_id = " + id + ";";

		rs = DatabaseConnector.select(mysqlQuery);
		List<ArticleCardData> articles = new ArrayList<ArticleCardData>();
		try {
			while (rs.next()) {
				ArticleCardData articleCard = new ArticleCardData();
				articleCard.setUrl(rs.getString("url"));
				articleCard.setTitle(rs.getString("title"));
				articleCard.setSectionId(rs.getInt("section"));
				articleCard.setContent(rs.getString("content"));
				articleCard.setDatetime(rs.getTimestamp("datetime").toString());
				articles.add(articleCard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/* 테스트를 위한 수동 데이터 하드코딩 */
		request.setAttribute("reporter", reporterCard);
		
		request.setAttribute("articles", articles);
		
		
		
		/* ******** !!IMPORTANT!! **********
		 * dispatch 시, 절대경로를 사용할 것!!!
		 * 상대경로로 dispatch할 경우, /reporters/ShowReporter로 전달됨
		 * 따라서 web.xml의 "/reporters/*"패턴에 붙잡혀 다시  서블릿으로 전달됨
		 * 즉, 무한루프를 돌게 됨!!!!
		 */
		RequestDispatcher rd = request.getRequestDispatcher("/ShowReporter.jsp");
		rd.forward(request, response);
	}

}
