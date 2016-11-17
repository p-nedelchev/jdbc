package taskfive;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.ds.PGPoolingDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class AddressBookTest {
    public Connection connection;
    public AddressBook addressBook;

    @Before
    public void setUp() throws Exception {
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setServerName("localhost");
        source.setUser("postgres");
        source.setPassword("postgres");
        connection = source.getConnection();
        createRepository();
        addressBook = new AddressBook(source);
    }

    @After
    public void tearDown() throws Exception {
        deleteRepository();
    }

    @Test
    public void usersRetrieved() throws Exception {
        User testUser = new User("John", "Smith", 30);
        addressBook.add(testUser);
        List<User> users = addressBook.findAllUsers();
        assertThat(users.size(), is(1));
        assertThat(users, hasItem(testUser));
    }

    @Test
    public void contactsRetrieved() throws Exception {
        Contact testContact = new Contact("John", "Dow", "test@teast.com", "123456789");
        addressBook.add(testContact);
        List<Contact> contacts = addressBook.findAllContacts();
        assertThat(contacts.size(), is(1));
        assertThat(contacts, hasItem(testContact));
    }

    @Test
    public void addressRetrieved() throws Exception {
        Address testAddress = new Address("street", "city", 1234);
        addressBook.add(testAddress);
        List<Address> addresses = addressBook.findAllAddresses();
        assertThat(addresses.size(), is(1));
        assertThat(addresses, hasItem(testAddress));
    }

    public void createRepository() {
        try (Statement statement = connection.createStatement()){
            String createUserTable =
                    new String(Files.readAllBytes(Paths.get("./schema/users_ddl.sql")));
            String createAddressTable =
                    new String(Files.readAllBytes(Paths.get("./schema/address_ddl.sql")));
            String createContactTable =
                    new String(Files.readAllBytes(Paths.get("./schema/contact_ddl.sql")));
            statement.execute(createUserTable);
            statement.execute(createAddressTable);
            statement.execute(createContactTable);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRepository() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE users CASCADE");
            statement.execute("DROP TABLE addresses CASCADE");
            statement.execute("DROP TABLE contacts CASCADE");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
