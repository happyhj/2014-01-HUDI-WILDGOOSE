package next.wildgoose.web;

import java.io.IOException;
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
		/* 테스트를 위한 수동 데이터 하드코딩 */
		String name = "양현석";
		String email = "fourwingsy@nhnnext.org";
		String pressName = "nextian";
		String info = "양현석 기자 / fourwingsy@nhnnext.org";
		
		request.setAttribute("name", name);
		request.setAttribute("email", email);
		request.setAttribute("pressName", pressName);
		request.setAttribute("info", info);
		request.getRequestDispatcher("ShowReporter.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
