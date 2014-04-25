package next.wildgoose.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.wildgoose.dao.ArticleCardDAO;
import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.model.ArticleCard;
import next.wildgoose.model.ReporterCard;
import next.wildgoose.utility.Wildgoose;


public class ShowReporter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowReporter.class.getName());
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher reqDispatcher = null;
		
		ReporterCard reporterCardData = null;
		List<ArticleCard> articleCards = null;
		ReporterCardDAO reporterCardDao = null;
		ArticleCardDAO articleCardDao = null;
		String rawReporterId = null;
		
		/* 
		 * getRequestURI() : /reporters/HBKim1
		 * getRequestURL() : http://10.73.45.145:8080/reporters/HBKim1
		 */
		String uri = request.getRequestURI();
		
		// id가 입력되지 않은 경우 처리
		if ("/reporters".equals(uri) || "/reporters/".equals(uri)) {
			// error page
			reqDispatcher = request.getRequestDispatcher("/" + Wildgoose.ERROR_PAGE);
			reqDispatcher.forward(request, response);
			return;
		}
		
		rawReporterId = uri.substring("/reporters/".length());
		int reporterId = Integer.parseInt(rawReporterId);
		
		// DB에서 id로 검색하여 reporterCardData 가져오기
		reporterCardDao = new ReporterCardDAO();
		try {
			reporterCardData = reporterCardDao.findReporterById(reporterId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// DB에서 id으로 검색하여 reporter의 최신 기사 리스트 가져오기
		articleCardDao = new ArticleCardDAO();
		articleCards = articleCardDao.findArticlesById(reporterId);
		
		request.setAttribute("reporter", reporterCardData);		
		request.setAttribute("articles", articleCards);
		
		// ******** !!IMPORTANT!! **********
		reqDispatcher = request.getRequestDispatcher("/" + Wildgoose.SHOW_PAGE);
		reqDispatcher.forward(request, response);
	}
}
