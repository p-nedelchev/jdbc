package tasktwo;

import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public interface TripRepository {
    Person addPerson(Person person);
    Trip addTrip(Trip trip);
    Person updatePersonInfo(Person person);
    Trip updateTrip(Trip trip);
    List<Person> allPeople();
    List<Trip> allTrips();
}
