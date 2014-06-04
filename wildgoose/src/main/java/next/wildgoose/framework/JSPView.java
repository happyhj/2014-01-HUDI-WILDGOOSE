package next.wildgoose.framework;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.wildgoose.utility.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSPView implements View {
	private static final Logger LOGGER = LoggerFactory.getLogger(JSPView.class.getName());

	@Override
	public void show(HttpServletRequest request, HttpServletResponse response, Uri uri, Result resultData) throws ServletException, IOException{
		ServletContext context = request.getServletContext();
		String jspName = pickJsp(context, uri, resultData);
		LOGGER.debug("jspFileName " + jspName);
		
		request.setAttribute("data", resultData);
		RequestDispatcher reqDispatcher = request.getRequestDispatcher("/" + jspName);
		reqDispatcher.forward(request, response);
	}

	private String pickJsp(ServletContext context, Uri uri, Result resultData) {
		Map<Uri, String> jspMap = (Map<Uri, String>) context.getAttribute("jspMap");
		
		//// JSPView의 경우 이 과정에서 내부적으로 대응하는 .jsp 파일을 멤버로 확보하도록 한다.
		if (resultData == null || resultData.getStatus() != 200) {
			return jspMap.get(null);
		}
		
		Uri keyUri = getKey(jspMap.keySet(), uri);
		String result = jspMap.get(keyUri);
		
		return result;
	}

	private Uri getKey(Set<Uri> keySet, Uri uri) {
		Uri keySchema = null;
		Iterator<Uri> schemaIr = keySet.iterator();
		
		while (keySchema == null && schemaIr.hasNext()) {
			Uri schema = schemaIr.next();
			if (schema == null) {
				continue;
			}
			
			if (schema.size() != uri.size()) {
				continue;
			}
			for (int i=schema.size()-1; i>=0; --i) {
				String subSchema = schema.get(i);
				
				if ("*".equals(subSchema)) {
					continue;
				}
				
				if (subSchema.equals(uri.get(i)) == false) {
					break;
				}
				
				keySchema = schema;
			}
		}
		return keySchema;
	}

}
