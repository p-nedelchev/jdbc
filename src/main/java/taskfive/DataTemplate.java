package taskfive;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class DataTemplate {
    private DataSource source;

    public DataTemplate(DataSource source) {
        this.source = source;
    }

    public void add(String query, ParameterBinder binder) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            binder.bindParameters(preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T>List<T> rowMapper(String query, RowMapper<T> rowMapper) {
        List<T> allRecords = new LinkedList<T>();
        try (Connection connection = source.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            while (result.next()){
                T object = rowMapper.mapRow(result);
                allRecords.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allRecords;
    }
}
