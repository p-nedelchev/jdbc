package taskfive;

import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public interface AddressRepository {
    Address add(Address address);
    List<Address> findAllAddresses();
}
