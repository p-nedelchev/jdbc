package taskthree;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class VendorRepositoryTest {
    public Connection connection;
    public ConcreteVendorRepository vendorRepository;


    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/";
        connection = DriverManager.getConnection(connectionString, "postgres", "postgres");
        createRepository();
        vendorRepository = new ConcreteVendorRepository(connection);
    }

    @After
    public void tearDown() throws Exception {
        deleteRepository();
    }

    @Test
    public void historyPageFetched() throws Exception {
        populateHistory();
        int page = 3;
        int limit = 2;
        Vendor lastPageVendor = new Vendor("ebay", 2, "www.ebay.com", 1);
        List<Vendor> history = vendorRepository.history(page, limit);
        assertThat(history.size(), is(1));
        assertThat(history, hasItem(lastPageVendor));
    }

    public void populateHistory() {
        String query = "INSERT INTO vendor_history VALUES " +
                "(1, 'emac', 1, 'www.emac.com',1, now())," +
                "(2, 'amazon', 1, 'www.amazon.com', 1, now())," +
                "(3, 'aliexpress', 3, 'www.express.com', 1, now())," +
                "(4, 'metro', 2, 'www.metro.com', 1, now())," +
                "(5, 'ebay', 2, 'www.ebay.com', 1, now())";
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createRepository() {
        try {
            Statement statement = connection.createStatement();
            String createPeopleTable =
                    new String(Files.readAllBytes(Paths.get("./schema/vendor_ddl.sql")));
            String createTripsTable =
                    new String(Files.readAllBytes(Paths.get("./schema/vendor_history_ddl.sql")));
            String createTrigFunc =
                    new String(Files.readAllBytes(Paths.get("./schema/vendor_history_func.sql")));
            String createTrigger = 
                    new String(Files.readAllBytes(Paths.get("./schema/vendor_history_trigger.sql")));
            statement.execute(createPeopleTable);
            statement.execute(createTripsTable);
            statement.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }    
    
    public void deleteRepository() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE vendor CASCADE");
            statement.execute("DROP TABLE vendor_history CASCADE");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }        
    }
}
