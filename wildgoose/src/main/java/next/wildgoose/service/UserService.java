package next.wildgoose.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import next.wildgoose.dao.FavoriteDAO;
import next.wildgoose.utility.Uri;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService implements Daction {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());


	@Override
	public DactionResult execute(HttpServletRequest request) {
		Uri uri = new Uri(request);
		DactionResult result;
		JSONObject json = new JSONObject();
		json.put("text", "잘못된 접근입니다");
		//uri = api/v1/user/reporters
		HttpSession session = request.getSession();
		String methodType = request.getMethod();//POST or DELTE확인 가능
		String reporterId = request.getParameter("reporter_id");
		String email = (String) session.getAttribute("userId");
		
		ServletContext context = request.getServletContext();
		FavoriteDAO favDao = (FavoriteDAO) context.getAttribute("FavoriteDAO");

		if(uri.check(3, "reporters")){
			//session 에서 user의 email을 확인함
			if(email == null){
				result = new DactionResult("text", json);
				return result;
			}
			
			//methodType확인
			if("POST".equals(methodType)){
				//필요한 파라미터: reporter_id, user_email
				if (favDao.addFavorite(reporterId, email)) {
					// 잘 들어감
					json.put("text", "success");
				} else {
					json.put("text", "failed");
				}
			} else if ("DELETE".equals(methodType)) {
				if (favDao.removeFavorite(reporterId, email)) {
					// 잘 삭제 됨
					json.put("text", "success");
				} else {
					json.put("text", "failed");
				}
			}
		}
		result = new DactionResult("text", json);
		return result;
	}

}
