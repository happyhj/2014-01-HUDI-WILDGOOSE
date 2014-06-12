package next.wildgoose.framework.dao.template;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter {
	public abstract void setValues(PreparedStatement psmt) throws SQLException;
}
