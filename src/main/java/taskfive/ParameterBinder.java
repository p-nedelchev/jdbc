package taskfive;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public interface ParameterBinder {
    void bindParameters(PreparedStatement preparedStatement) throws SQLException;
}
