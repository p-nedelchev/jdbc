package taskthree;

import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public interface VendorRepository {
    Vendor add(Vendor vendor);
    Vendor updateVendor(Vendor vendor);
    List<Vendor> allVendors();
    List<Vendor> history(int pageNumber, int limit);
}
