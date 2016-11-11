package tasktwo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static java.sql.DriverManager.getConnection;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class TripRepositoryTest {
    public TripRepository tripRepository;
    public Connection connection;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/";
        connection = getConnection(connectionString, "postgres", "postgres");
        tripRepository = new TripRepository(connection);
        tripRepository.createRepository();
    }

    @After
    public void tearDown() throws Exception {
        tripRepository.deleteRepository();
    }

    @Test
    public void personAdded() throws Exception {
        Person petar = new Person("Petar", "0123456789", 27, "test@domain.com");
        tripRepository.addPerson(petar);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM people WHERE egn ='" + petar.egn+"'");
        resultSet.next();
        assertThat(resultSet.getString("name"), is(petar.name));
        assertThat(resultSet.getString("egn"), is(petar.egn));
        assertThat(resultSet.getInt("age"), is(petar.age));
        assertThat(resultSet.getString("email"), is(petar.email));
    }

    @Test
    public void tripAdded() throws Exception {
        Statement statement = connection.createStatement();
        Person petar = new Person("Petar", "0123456789", 27, "test@domain.com");
        Trip trip = new Trip("0123456789",LocalDate.of(2016,11,1) ,LocalDate.of(2016,11,2) , "New York");
        tripRepository.addPerson(petar);
        tripRepository.addTrip(trip);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM trips WHERE egn ='" + trip.egn+"'");
        resultSet.next();
        assertThat(resultSet.getString("egn"), is(trip.egn));
        assertThat(resultSet.getDate("arrival_date"), is(Date.valueOf((trip.dateOfArrival))));
        assertThat(resultSet.getDate("departure_date"), is(Date.valueOf((trip.dateOfDeparture))));
        assertThat(resultSet.getString("city"), is(trip.city));
    }

    @Test
    public void personUpdated() throws Exception {
        Statement statement = connection.createStatement();
        Person john = new Person("John", "0123456789", 20, "test@domain.com");
        tripRepository.addPerson(john);
        Person johnAgeUpdated = new Person("John", "0123456789", 25, "test@domain.com");
        tripRepository.updatePersonInfo(johnAgeUpdated);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM people WHERE egn ='" + johnAgeUpdated.egn+"'");
        resultSet.next();
        assertThat(resultSet.getInt("age"), is(johnAgeUpdated.age));
    }

    @Test
    public void tripUpdated() throws Exception {
        setData();
        Trip georgeTripUpdated = new Trip("3333333333", LocalDate.of(2016, 11, 3), LocalDate.of(2016,11,8), "VN");
        tripRepository.addTrip(georgeTripUpdated);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM trips WHERE egn ='" + georgeTripUpdated.egn+"'");
        resultSet.next();
    }

    @Test
    public void allPeopleReturned() throws Exception {
        Person john = new Person("John", "0123456789", 35, "test@domain.com");
        Person paul = new Person("Paul", "9876543210", 20, "test@domain.com");
        tripRepository.addPerson(john);
        tripRepository.addPerson(paul);
        List<Person> allPeople = tripRepository.allPeople();
        assertThat(allPeople.get(0), is(john));
        assertThat(allPeople.get(1), is(paul));
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
        assertThat(foundPeople.get(0), is(person1));
        assertThat(foundPeople.get(1), is(person2));
    }

    @Test
    public void peopleAtSamePlace() throws Exception {
        Person testPerson1 = new Person("John", "111111111", 22, "test@domain.com");
        Person testPerson2 = new Person("Paul", "2222222222", 25, "test@domain.com");
        Trip johnTrip = new Trip(testPerson1.egn, LocalDate.of(2016, 11, 1), LocalDate.of(2016,11,5), "VT");
        Trip paulTrip = new Trip(testPerson2.egn, LocalDate.of(2016, 11, 3), LocalDate.of(2016,11,8), "VT");
        tripRepository.addPerson(testPerson1);
        tripRepository.addPerson(testPerson2);
        tripRepository.addTrip(johnTrip);
        tripRepository.addTrip(paulTrip);
        List<Person> peopleAtTheSameCity = tripRepository.sameCityPeople(LocalDate.of(2016, 11, 4), "VT");
        assertThat(peopleAtTheSameCity.get(0), is(testPerson1));
        assertThat(peopleAtTheSameCity.get(1), is(testPerson2));
    }

    @Test
    public void citiesFetchedByAttendance() throws Exception {
        setData();
        List<String> cities = tripRepository.citiesByAttendance();
        assertThat(cities.get(0), is("VT"));
        assertThat(cities.get(1), is("SF"));
    }


    public void setData() {
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
