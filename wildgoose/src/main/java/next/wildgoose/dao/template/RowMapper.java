package next.wildgoose.dao.template;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {
	public abstract Object mapRow(ResultSet rs) throws SQLException ;
}