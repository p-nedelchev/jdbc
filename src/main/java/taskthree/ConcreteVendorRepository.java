package taskthree;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class ConcreteVendorRepository implements VendorRepository {
    private Connection connection;

    public ConcreteVendorRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Vendor add(Vendor vendor) {
        String query = "INSERT INTO vendor(name, rating,website, active_flag) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vendor.name);
            preparedStatement.setInt(2, vendor.rating);
            preparedStatement.setString(3, vendor.website);
            preparedStatement.setInt(4, vendor.active_flag);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendor;
    }

    @Override
    public Vendor updateVendor(Vendor vendor) {
        String query = "UPDATE vendor SET rating=?, website =?, active_flag=? where name=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, vendor.rating);
            preparedStatement.setString(2, vendor.website);
            preparedStatement.setInt(3, vendor.active_flag);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendor;
    }

    @Override
    public List<Vendor> allVendors() {
        List<Vendor> allVendors = new LinkedList<>();
        String query = "SELECT * FROM vendor";
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                String name = result.getString("name");
                Integer rating = result.getInt("rating");
                String website = result.getString("website");
                Integer active_rating = result.getInt("active_rating");
                Vendor vendor = new Vendor(name, rating,website,active_rating);
                allVendors.add(vendor);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allVendors;
    }

    @Override
    public List<Vendor> history(int pageNumber, int limit) {
        List<Vendor> page = new LinkedList<>();
        String query = "SELECT * FROM vendor_history ORDER BY date_changed LIMIT ? OFFSET ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, limit);
            statement.setInt(2, limit*(pageNumber -1));
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String name = result.getString("name");
                Integer rating = result.getInt("rating");
                String website = result.getString("website");
                Integer active_rating = result.getInt("active_flag");
                Vendor vendor = new Vendor(name, rating,website,active_rating);
                page.add(vendor);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return page;
    }

}
