package tasktwo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class TripRepository implements Repository{

    private Connection connection;

    public TripRepository(Connection connection){
        this.connection = connection;
    }

    public void createRepository() {
        try {
            Statement statement = connection.createStatement();
            String createPeopleTable = new String(Files.readAllBytes(Paths.get("./schema/people_ddl.sql")));
            String createTripsTable = new String(Files.readAllBytes(Paths.get("./schema/trips_ddl.sql")));
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

    public Person addPerson(Person person) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO people VALUES(?,?,?,?)");
            preparedStatement.setString(1, person.name);
            preparedStatement.setString(2,person.egn);
            preparedStatement.setInt(3, person.age);
            preparedStatement.setString(4, person.email);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public Trip addTrip(Trip trip) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trips VALUES(?,?,?,?)");
            preparedStatement.setString(1, trip.egn);
            preparedStatement.setDate(2, Date.valueOf(trip.dateOfArrival));
            preparedStatement.setDate(3, Date.valueOf(trip.dateOfDeparture));
            preparedStatement.setString(4, trip.city);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trip;
    }


    public Person updatePersonInfo(Person person) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE people SET name = ?, age = ?, email = ? WHERE egn= ?");
            preparedStatement.setString(1, person.name);
            preparedStatement.setInt(2 , person.age);
            preparedStatement.setString(3, person.email);
            preparedStatement.setString(4, person.egn);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public Trip updateTrip(Trip trip) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE trips SET arrival_date = ?, departure_date = ?, city = ? WHERE egn= ?");
            preparedStatement.setDate(1, Date.valueOf(trip.dateOfArrival));
            preparedStatement.setDate(2 , Date.valueOf(trip.dateOfDeparture));
            preparedStatement.setString(3, trip.city);
            preparedStatement.setString(4, trip.egn);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trip;
    }

    public List<Person> allPeople() {
        List<Person> allPeople = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM people");
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                String egn = resultSet.getString("egn");
                Integer age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                Person person = new Person(name, egn, age, email);
                allPeople.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPeople;
    }

    public List<Trip> allTrips() {
        List<Trip> allTrips = new LinkedList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM trips");
            while(resultSet.next()) {
                Date dateOfArrival = resultSet.getDate("arrival_date");
                String egn = resultSet.getString("egn");
                Date dateOfDeparture = resultSet.getDate("departure_date");
                String city = resultSet.getString("email");
                Trip trip = new Trip(egn, dateOfArrival.toLocalDate(), dateOfDeparture.toLocalDate(), city);
                allTrips.add(trip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allTrips;
    }


    public List<Person> searchPeople(String name) {
        List<Person> foundPeople = new LinkedList<>();
        String search = "SELECT * FROM people WHERE name LIKE '" + name + "%'";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(search);
            while (result.next()) {
                String newName = result.getString("name");
                String newEgn = result.getString("egn");
                Integer newAge = result.getInt("age");
                String newEmail = result.getString("email");
                Person foundPerson = new Person(newName, newEgn, newAge, newEmail);
                foundPeople.add(foundPerson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundPeople;
    }


    public List<Person> sameCityPeople(LocalDate date, String city) {
        List<Person> people = new LinkedList<>();
        String query = "SELECT * FROM trips " +
                "INNER JOIN people " +
                "ON people.egn = trips.egn " +
                "WHERE arrival_date < ? and ? < departure_date and city = ?";
        try {
            PreparedStatement prStatement = connection.prepareStatement(query);
            prStatement.setDate(1, Date.valueOf(date));
            prStatement.setDate(2, Date.valueOf(date));
            prStatement.setString(3, city);
            ResultSet result = prStatement.executeQuery();
            while(result.next()) {
                String name = result.getString("name");
                String egn = result.getString("egn");
                Integer age = result.getInt("age");
                String email = result.getString("email");
                Person matchedPerson = new Person(name, egn, age, email);
                people.add(matchedPerson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public List<String> citiesByAttendance() {
        List<String> citiesByAttendance = new LinkedList<>();
        String query = "SELECT city, COUNT(city) from trips GROUP BY city";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String city = resultSet.getString("city");
                citiesByAttendance.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  citiesByAttendance;
    }
}
