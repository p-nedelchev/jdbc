package tasktwo;


import java.time.LocalDate;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class Trip {
    public final String egn;
    public final LocalDate dateOfArrival;
    public final LocalDate dateOfDeparture;
    public final String city;

    public Trip(String egn, LocalDate dateOfArrival, LocalDate dateOfDeparture, String city) {
        this.egn = egn;
        this.dateOfArrival = dateOfArrival;
        this.dateOfDeparture = dateOfDeparture;
        this.city = city;
    }
}
