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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;

        Trip trip = (Trip) o;

        if (egn != null ? !egn.equals(trip.egn) : trip.egn != null) return false;
        if (dateOfArrival != null ? !dateOfArrival.equals(trip.dateOfArrival) : trip.dateOfArrival != null)
            return false;
        if (dateOfDeparture != null ? !dateOfDeparture.equals(trip.dateOfDeparture) : trip.dateOfDeparture != null)
            return false;
        return city != null ? city.equals(trip.city) : trip.city == null;

    }

    @Override
    public int hashCode() {
        int result = egn != null ? egn.hashCode() : 0;
        result = 31 * result + (dateOfArrival != null ? dateOfArrival.hashCode() : 0);
        result = 31 * result + (dateOfDeparture != null ? dateOfDeparture.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
