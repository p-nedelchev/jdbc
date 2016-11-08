package taskone;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class BookRepository {
    private Connection connection;
    private Statement statement;
    private String tableName;

    public BookRepository(Connection connection, String tableName) throws SQLException {
        this.connection = connection;
        this.tableName = tableName;
        statement = connection.createStatement();
    }

    public List<Book> selectAllBooks() {
        List<Book> books = new LinkedList<>();
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        while (resultSet.next()) {
            String bookName = resultSet.getString("book_name");
            String author = resultSet.getString("author");
            int year = resultSet.getInt("year");
            Book book = new Book(bookName, author, year);
            books.add(book);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> searchBook(String name) {
        List<Book> books = new LinkedList<>();
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM " + tableName+ " WHERE book_name LIKE '%" + name + "%'");
            while (resultSet.next()) {
                String bookName = resultSet.getString("book_name");
                String bookAuthor = resultSet.getString("author");
                int year = resultSet.getInt("year");
                Book book = new Book(bookName, bookAuthor, year);
                books.add(book);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public int updateBook(Book book)  {
        PreparedStatement preparedStatement = null;
        String updateQuery = "UPDATE " + tableName +" SET author= ?, year = ? WHERE book_name = ? ";
        int affectedRows = 0;
        try {
            preparedStatement =connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, book.author);
            preparedStatement.setInt(2, book.year);
            preparedStatement.setString(3, book.bookName);
            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    public int deleteBook(String bookName)  {
        PreparedStatement preparedStatement = null;
        int deletedRows = 0;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " WHERE book_name = ? ");
            preparedStatement.setString(1, bookName);
            deletedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedRows;
    }

    public Book addBook(Book book) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + "(book_name,author,year) VALUES (?, ?, ?)");
            preparedStatement.setString(1, book.bookName);
            preparedStatement.setString(2, book.author);
            preparedStatement.setInt(3, book.year);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }

    public int addBooks(List<Book> books) {
        PreparedStatement preparedStatement = null;
        int rowsAdded = 0;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO " + tableName + "(book_name,author,year) VALUES (?, ?, ?)");
            for (Book book : books) {
                preparedStatement.setString(1, book.bookName);
                preparedStatement.setString(2, book.author);
                preparedStatement.setInt(3, book.year);
                rowsAdded += preparedStatement.executeUpdate();
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAdded;
    }

    public void addColumn(String columnName, JDBCType type) {
        String query = "ALTER TABLE books ADD " + columnName + " " + type.getName();
        try {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropColumn(String columnName) {
        String query = "ALTER TABLE books DROP " + columnName;
        try {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
