package next.wildgoose.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.model.ReporterCard;
import next.wildgoose.utility.Wildgoose;


public class SearchReporter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher reqDispatcher = null;
		
		String searchQuery = null;
		List<ReporterCard> reporterCards = null;
		ReporterCardDAO reporterCardDao = null;
		
		// 첫 요청시, 'q'가 null인 경우
		searchQuery = request.getParameter("q");
		if (searchQuery == null) {
			reqDispatcher = request.getRequestDispatcher(Wildgoose.SUCCESS_PAGE);
			reqDispatcher.forward(request, response);
			return;	
		}
		
		// encoding 'utf-8'
		searchQuery = new String(searchQuery.getBytes("8859_1"), "UTF-8");
		searchQuery = searchQuery.trim();
		
		// 유효하지 않은 'q'
		if (searchQuery.equals("")) {
			reqDispatcher = request.getRequestDispatcher(Wildgoose.ERROR_PAGE);
			reqDispatcher.forward(request, response);
			return;
		}
		
		// DB에서 이름으로 검색하여 reporterCard 리스트 가져오기
		reporterCardDao = new ReporterCardDAO();
		reporterCards = reporterCardDao.findReportersByName(searchQuery.toString());
		
		request.setAttribute("reporterCards", reporterCards);
		request.setAttribute("searchQuery", searchQuery);
		
		reqDispatcher = request.getRequestDispatcher(Wildgoose.SUCCESS_PAGE);
		reqDispatcher.forward(request, response);
	}
}
