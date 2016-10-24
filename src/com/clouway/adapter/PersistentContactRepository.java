package com.clouway.adapter;

import com.clouway.core.Contact;
import com.clouway.core.ContactRepository;
import com.clouway.datastore.DataStore;

import java.util.List;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class PersistentContactRepository implements ContactRepository {

  private final DataStore dataStore;

  public PersistentContactRepository(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public void register(Contact contact) {
    String query="INSERT INTO CONTACT (?,?,?)";
    dataStore.update(query,contact.name,contact.phone,contact.email);
  }

  @Override
  public List<Contact> getAll() {
    String query="SELECT * FROM CONTACT";
    return dataStore.fetchRows(query,resultSet -> new Contact(resultSet.getString(1),resultSet.getInt(2),resultSet.getString(3)));
  }
}
