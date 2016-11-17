package taskfive;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class AddressBook implements UserRepository, AddressRepository, ContactRepository {
    private DataTemplate dataTemplate;

    public AddressBook(DataSource source) {
        this.dataTemplate = new DataTemplate(source);
    }

    @Override
    public Address add(Address address) {
        String query = "INSERT INTO addresses(address_line, city, post_code) VALUES (?, ?, ?)";
        dataTemplate.add(query, new ParameterBinder() {
            @Override
            public void bindParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, address.addressLine);
                preparedStatement.setString(2, address.city);
                preparedStatement.setInt(3, address.postCode);
            }
        });
        return address;
    }

    @Override
    public List<Address> findAllAddresses() {
        String query = "SELECT address_line, city, post_code FROM addresses";
        return dataTemplate.rowMapper(query, result ->
                new Address(result.getString("address_line"), result.getString("city"), result.getInt("post_code")));
    }

    @Override
    public Contact add(Contact contact) {
        String query = "INSERT INTO contacts(first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
        dataTemplate.add(query, new ParameterBinder() {
            @Override
            public void bindParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, contact.firstName);
                preparedStatement.setString(2, contact.lastName);
                preparedStatement.setString(3, contact.email);
                preparedStatement.setString(4, contact.phone);
            }
        });
        return contact;
    }

    @Override
    public List<Contact> findAllContacts() {
        String query = "SELECT first_name, last_name, email, phone FROM contacts";
        return dataTemplate.rowMapper(query, result ->
                new Contact(result.getString("first_name"), result.getString("last_name"), result.getString("email"), result.getString("phone")));
    }

    @Override
    public User add(User user) {
        String query = "INSERT INTO users(first_name, last_name, age) VALUES (?, ?, ?)";
        dataTemplate.add(query, new ParameterBinder() {
            @Override
            public void bindParameters(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, user.firstName);
                preparedStatement.setString(2, user.lastName);
                preparedStatement.setInt(3, user.age);
            }
        });
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        String query = "SELECT first_name, last_name, age FROM users";
        return dataTemplate.rowMapper(query, result ->
                new User(result.getString("first_name"), result.getString("last_Name"), result.getInt("age")));
    }
}
