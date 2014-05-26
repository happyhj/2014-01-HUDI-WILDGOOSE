package next.wildgoose.backcontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import next.wildgoose.dao.ReporterCardDAO;
import next.wildgoose.dto.ReporterCard;
import next.wildgoose.dto.SearchResult;

public class SearchController implements BackController {

	@Override
	public Object execute(HttpServletRequest request) {
		SearchResult result = new SearchResult();
		List<ReporterCard> list = new ArrayList<ReporterCard>();
		
		ReporterCardDAO rcd = new ReporterCardDAO();
		list.add(rcd.findReporterById(2));
		list.add(rcd.findReporterById(22));
		list.add(rcd.findReporterById(222));
		
		String searchQuery = request.getParameter("q");
		
		System.out.println("list: " + list);

		result.setReporterCards(list);
		result.setSearchQuery(searchQuery);
		return result;
	}

}
