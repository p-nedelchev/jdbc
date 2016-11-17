package taskfive;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public interface RowMapper<T> {
    T mapRow(ResultSet result) throws SQLException;
}
