package next.wildgoose.framework;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.framework.utility.Utility;
import next.wildgoose.utility.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONView implements View {
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONView.class.getName());

	@Override
	public void show(HttpServletRequest request, HttpServletResponse response, Result resultData){
		// TODO Auto-generated method stub
		ServletContext context = request.getServletContext();
		String jsonString = Utility.toJsonString(resultData);
		LOGGER.debug(jsonString);
		LOGGER.debug(resultData.toString());
		PrintWriter out = null;

		response.setCharacterEncoding(context.getInitParameter("encoding"));
		response.setContentType(Constants.HEADER_CON_TYPE_JSON);

		try {
			out = response.getWriter();
			out.println(jsonString);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		out.close();	
	}
}
