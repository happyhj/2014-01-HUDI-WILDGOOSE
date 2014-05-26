package next.wildgoose.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSPView implements View {

	private String target;

	@Override
	public void show(Map<String, Object> resultData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("data", resultData);
		System.out.println("target: " + target);
		RequestDispatcher reqDispatcher = request.getRequestDispatcher(target);
		reqDispatcher.forward(request, response);
	}

	public void setTarget(String target) {
		this.target = target;		
	}


}
