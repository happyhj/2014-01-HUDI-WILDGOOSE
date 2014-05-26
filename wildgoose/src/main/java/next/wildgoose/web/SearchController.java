package next.wildgoose.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ReporterCard;

public class SearchController implements BackController {

	@Override
	public Map<String, Object> execute(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<ReporterCard> list = new ArrayList<ReporterCard>();
		
		ReporterCardDAO rcd = new ReporterCardDAO();
		list.add(rcd.findReporterById(2));
		list.add(rcd.findReporterById(22));
		list.add(rcd.findReporterById(222));
		
		String searchQuery = request.getParameter("q");
		
		System.out.println("list: " + list);

		result.put("searchQuery", searchQuery);	
		result.put("reporterCards", list);
		return result;
	}

}
