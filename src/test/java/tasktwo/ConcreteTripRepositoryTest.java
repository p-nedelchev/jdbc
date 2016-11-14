package tasktwo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static java.sql.DriverManager.getConnection;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class ConcreteTripRepositoryTest {
    public ConcreteTripRepository tripRepository;
    public Connection connection;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/";
        connection = getConnection(connectionString, "postgres", "postgres");
        tripRepository = new ConcreteTripRepository(connection);
        createRepository();
    }

    @After
    public void tearDown() throws Exception {
        deleteRepository();
    }

    @Test
    public void personAdded() throws Exception {
        Person petar = new Person("Petar", "0123456789", 27, "test@domain.com");
        tripRepository.addPerson(petar);
        Statement statement = connection.createStatement();
        List<Person> foundPeople = tripRepository.searchPeople(petar.name);
        assertThat(foundPeople.size(), is(1));
        assertThat(foundPeople.get(0), is(petar));
    }

    @Test
    public void tripAdded() throws Exception {
        Person petar = new Person("Petar", "0123456789", 27, "test@domain.com");
        Trip trip = new Trip("0123456789",LocalDate.of(2016,11,1) ,LocalDate.of(2016,11,2) , "New York");
        tripRepository.addPerson(petar);
        tripRepository.addTrip(trip);
        List<Trip> foundTrips = tripRepository.allTrips();
        assertThat(foundTrips.size(), is(1));
        assertThat(foundTrips.get(0), is(trip));
    }

    @Test
    public void personUpdated() throws Exception {
        Person john = new Person("John", "0123456789", 20, "test@domain.com");
        tripRepository.addPerson(john);
        Person johnAgeUpdated = new Person("John", "0123456789", 25, "test@domain.com");
        tripRepository.updatePersonInfo(johnAgeUpdated);
        List<Person> foundPeople = tripRepository.allPeople();
        assertThat(foundPeople.size(), is(1));
        assertThat(foundPeople.get(0).age, is(johnAgeUpdated.age));
    }

    @Test
    public void tripUpdated() throws Exception {
        Person george = new Person("george","3333333333", 18, "test@domain.com" );
        Trip georgeTrip = new Trip(george.egn, LocalDate.of(2016, 11, 3), LocalDate.of(2016,11,8), "SF");
        Trip georgeTripUpdated = new Trip("3333333333", LocalDate.of(2016, 11, 3), LocalDate.of(2016,11,8), "VN");
        tripRepository.addPerson(george);
        tripRepository.addTrip(georgeTrip);
        tripRepository.updateTrip(georgeTripUpdated);
        List<Trip> foundTrips = tripRepository.allTrips();
        assertThat(foundTrips.size(), is(1));
        assertThat(foundTrips, hasItem(georgeTripUpdated) );
    }

    @Test
    public void allPeopleReturned() throws Exception {
        Person john = new Person("John", "0123456789", 35, "test@domain.com");
        Person paul = new Person("Paul", "9876543210", 20, "test@domain.com");
        tripRepository.addPerson(john);
        tripRepository.addPerson(paul);
        List<Person> allPeople = tripRepository.allPeople();
        assertThat(allPeople.size(), is(2));
        assertThat(allPeople, hasItem(john));
        assertThat(allPeople, hasItem(paul));
    }

    @Test
    public void searchedPeopleFound() throws Exception {
        Person person1 = new Person("abcd","111111111", 19, "test@domain.com");
        Person person2 = new Person("abdfgh","222222222", 19, "test@domain.com");
        Person person3 = new Person("addfgh","3333333333", 19, "test@domain.com");
        tripRepository.addPerson(person1);
        tripRepository.addPerson(person2);
        tripRepository.addPerson(person3);
        List<Person> foundPeople = tripRepository.searchPeople("ab");
        assertThat(foundPeople.size(), is(2));
        assertThat(foundPeople, hasItem(person1));
        assertThat(foundPeople, hasItem(person2));
    }

    @Test
    public void peopleAtSamePlace() throws Exception {
        Person testPerson1 = new Person("John", "111111111", 22, "test@domain.com");
        Person testPerson2 = new Person("Paul", "2222222222", 25, "test@domain.com");
        Person testPerson3 = new Person("Ian", "333333333", 24, "test@domain.com");
        Trip johnTrip = new Trip(testPerson1.egn, LocalDate.of(2016, 11, 1), LocalDate.of(2016,11,6), "VT");
        Trip paulTrip = new Trip(testPerson2.egn, LocalDate.of(2016, 11, 3), LocalDate.of(2016,11,8), "VT");
        Trip ianTrip = new Trip(testPerson3.egn, LocalDate.of(2016, 11, 5), LocalDate.of(2016, 11, 10),"VT");
        tripRepository.addPerson(testPerson1);
        tripRepository.addPerson(testPerson2);
        tripRepository.addPerson(testPerson3);
        tripRepository.addTrip(johnTrip);
        tripRepository.addTrip(paulTrip);
        tripRepository.addTrip(ianTrip);
        List<Person> peopleAtTheSameCity = tripRepository.sameCityPeople(LocalDate.of(2016, 11, 4), "VT");
        assertThat(peopleAtTheSameCity.size(), is(2));
        assertThat(peopleAtTheSameCity, hasItem(testPerson1));
        assertThat(peopleAtTheSameCity, hasItem(testPerson2));
    }

    @Test
    public void citiesFetchedByAttendance() throws Exception {
        populateRepository();
        List<String> cities = tripRepository.citiesByAttendance();
        assertThat(cities.get(0), is("VT"));
        assertThat(cities.get(1), is("SF"));
    }

    public void createRepository() {
        try {
            Statement statement = connection.createStatement();
            String createPeopleTable =
                    new String(Files.readAllBytes(Paths.get("./schema/people_ddl.sql")));
            String createTripsTable =
                    new String(Files.readAllBytes(Paths.get("./schema/trips_ddl.sql")));
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
            statement.execute("DROP TABLE trips CASCADE");
            statement.execute("DROP TABLE people CASCADE");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearRepository() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("TRUNCATE TABLE trips CASCADE");
            statement.execute("TRUNCATE TABLE people CASCADE");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateRepository() {
        Person testPerson1 = new Person("John", "111111111", 22, "test@domain.com");
        Person testPerson2 = new Person("Paul", "2222222222", 25, "test@domain.com");
        Person testPerson3 = new Person("George", "3333333333", 27, "test@domain.com");
        Trip johnTrip = new Trip(testPerson1.egn, LocalDate.of(2016, 11, 1), LocalDate.of(2016,11,5), "VT");
        Trip paulTrip = new Trip(testPerson2.egn, LocalDate.of(2016, 11, 3), LocalDate.of(2016,11,8), "VT");
        Trip georgeTrip = new Trip(testPerson3.egn, LocalDate.of(2016, 11, 3), LocalDate.of(2016,11,8), "SF");
        tripRepository.addPerson(testPerson1);
        tripRepository.addPerson(testPerson2);
        tripRepository.addPerson(testPerson3);
        tripRepository.addTrip(johnTrip);
        tripRepository.addTrip(paulTrip);
        tripRepository.addTrip(georgeTrip);
    }
}
