package taskfive;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class Address {
    public final String addressLine;
    public final String city;
    public final Integer postCode;

    public Address(String addressLine, String city, Integer postCode) {
        this.addressLine = addressLine;
        this.city = city;
        this.postCode = postCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (addressLine != null ? !addressLine.equals(address.addressLine) : address.addressLine != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        return postCode != null ? postCode.equals(address.postCode) : address.postCode == null;

    }

    @Override
    public int hashCode() {
        int result = addressLine != null ? addressLine.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (postCode != null ? postCode.hashCode() : 0);
        return result;
    }
}
