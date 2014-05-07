package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.pool.DataSource;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HookingKeywordDAO {
	private static HookingKeywordDAO hkDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(HookingKeywordDAO.class.getName());
	
	public static HookingKeywordDAO getInstance() {
		if (hkDao == null) {
			hkDao = new HookingKeywordDAO();
		}
		return hkDao;
	}
	
	public JSONObject getHookingKeywordsCount(int reporterId) {
		
		Connection conn = DataSource.getInstance().getConnection();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		JSONObject result = new JSONObject();
		
		StringBuilder query = new StringBuilder();
		query.append("select b.word, sum(b.count) as count from (");
		query.append("(select author_id, article_URL as url from article_author where author_id = ?) as a ");
		query.append("join (select article_URL as url, word, count from article_hooking_keyword ");
		query.append("join hooking_keyword on article_hooking_keyword.hooking_keyword_id = hooking_keyword.id) as b ");
		query.append("on a.url = b.url)");
		
		try {
			psmt = conn.prepareStatement(query.toString());
			psmt.setInt(1, reporterId);
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				JSONObject data = new JSONObject();
				String keyword = rs.getString("word");
				int count = rs.getInt("count");
				data.put(keyword, count);
				result.append("data", data);
			}
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(),sqle);
		} finally {
			SqlUtil.closePrepStatement(psmt);
			SqlUtil.closeResultSet(rs);
			SqlUtil.closeConnection(conn);
		}
		
		return result;
	}

	
}
