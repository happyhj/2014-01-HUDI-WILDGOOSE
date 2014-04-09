package next.wildgoose.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.model.Article;
import next.wildgoose.model.Reporter;

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
		String name = URLDecoder.decode(uri.substring("/reporters/".length()));
		request.setAttribute("name", name);
		
		/* 테스트를 위한 수동 데이터 하드코딩 */
		TestDatas testData = new TestDatas();
		Reporter reporter = testData.getReporter();
		request.setAttribute("reporter", reporter);
		
		List<Article> articles = testData.getArticles();
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
