package next.wildgoose.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String name = uri.substring("/reporters/".length());
		
		/* 테스트를 위한 수동 데이터 하드코딩 */
		String email = "fourwingsy@nhnnext.org";
		String pressName = "nextian";
		String info = "양현석 기자 / fourwingsy@nhnnext.org";
		
		request.setAttribute("name", name);
		request.setAttribute("email", email);
		request.setAttribute("pressName", pressName);
		request.setAttribute("info", info);
		
		/* ******** !!IMPORTANT!! **********
		 * dispatch 시, 절대경로를 사용할 것!!!
		 * 상대경로로 dispatch할 경우, /reporters/ShowReporter로 전달됨
		 * 따라서 web.xml의 "/reporters/*"패턴에 붙잡혀 다시  서블릿으로 전달됨
		 * 즉, 무한루프를 돌게 됨!!!!
		 */
		RequestDispatcher rd = request.getRequestDispatcher("/ShowReporter.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
