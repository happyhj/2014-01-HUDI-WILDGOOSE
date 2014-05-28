package next.wildgoose.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.Uri;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSPView implements View {
	private static final Logger LOGGER = LoggerFactory.getLogger(JSPView.class.getName());

	@Override
	public void show(Object resultData, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		

		String jspName = pickJsp(request);	
		
		
		request.setAttribute("data", resultData);
		RequestDispatcher reqDispatcher = request.getRequestDispatcher("/" + jspName);
		reqDispatcher.forward(request, response);
	}

	private String pickJsp(HttpServletRequest request) {
		//// JSPView의 경우 이 과정에서 내부적으로 대응하는 .jsp 파일을 멤버로 확보하도록 한다.
		ServletContext context = request.getServletContext();
		Uri uri = new Uri(request);
		String uriResourceSchema = getUriResourceSchema(uri);
		Map<String, String> jspMap = (Map<String, String>) context.getAttribute("jspMap");
		String jspFileName = jspMap.get(uriResourceSchema);
		
		LOGGER.debug("jspFileName " + jspFileName);
		
		if(jspFileName == null) {
			jspFileName = "error.jsp";
		}
		
		return jspFileName;
	}

	private String getUriResourceSchema(Uri uri) {
		int uriSize = uri.size();
		List<String> resourceList = new ArrayList<String>();
		for(int i=0; i<uriSize ;i+=2) {
			resourceList.add(uri.get(i));
		}
		String joinedResourceList = StringUtils.join(resourceList, "/");
		return joinedResourceList;
	}

}
