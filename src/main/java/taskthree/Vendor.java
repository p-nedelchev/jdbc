package taskthree;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class Vendor {
    public final String name;
    public final Integer rating;
    public final String website;
    public final int active_flag;

    public Vendor(String name, Integer rating, String website, int active_flag) {
        this.name = name;
        this.rating = rating;
        this.website = website;
        this.active_flag = active_flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendor)) return false;

        Vendor vendor = (Vendor) o;

        if (active_flag != vendor.active_flag) return false;
        if (name != null ? !name.equals(vendor.name) : vendor.name != null) return false;
        if (rating != null ? !rating.equals(vendor.rating) : vendor.rating != null) return false;
        return website != null ? website.equals(vendor.website) : vendor.website == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + active_flag;
        return result;
    }
}
