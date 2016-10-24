package com.clouway.datastore;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface RowFetcher<T> {
  T fetchRow(ResultSet resultSet) throws SQLException;
}
