package next.wildgoose.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import next.wildgoose.model.DataSource;
import next.wildgoose.utility.Utility;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HookingKeywordDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(HookingKeywordDAO.class.getName());
	
	public JSONObject getHookingKeywordsCount(int reporterId) {
		
		Connection conn = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		JSONObject result = new JSONObject();
		
		try {
			conn = DataSource.getInstance().getConnection();
			
			StringBuilder query = new StringBuilder();
			
			query.append("select b.word, sum(b.count) as count from (");
			query.append("(select author_id, article_URL as url from article_author where author_id = ?) as a ");
			query.append("join (select article_URL as url, word, count from article_hooking_keyword ");
			query.append("join hooking_keyword on article_hooking_keyword.hooking_keyword_id = hooking_keyword.id) as b ");
			query.append("on a.url = b.url)");
			
			psmt = conn.prepareStatement(query.toString());
			psmt.setInt(1, reporterId);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				String keyword = rs.getString("word");
				LOGGER.debug(keyword);
				if (keyword == null) {
					return null;
				}
				int count = rs.getInt("count");
				JSONObject data = new JSONObject().put(keyword, count);
				result.append("data", data);
			}
			LOGGER.debug(result.toString());
		} catch (SQLException sqle) {
			LOGGER.debug(sqle.getMessage(),sqle);
		} finally {
			Utility.closePrepStatement(psmt);
			Utility.closeResultSet(rs);
			Utility.closeConnection(conn);
		}
		
		return result;
	}
}
