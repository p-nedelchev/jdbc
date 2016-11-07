import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import taskone.Book;
import taskone.BookRepository;

import java.sql.*;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class BookRepositoryTest {
    private Connection connection;
    private BookRepository bookRepository;

    @Before
    public void setUp() throws Exception {
        connection = getConnection();
        createRepository();
        bookRepository = new BookRepository(connection, "books_test");
        Book book1 = new Book("The Name of the Wind", "Patrick Rothfuss", 2007);
        Book book2 = new Book("Eragon", "Christopher Paulini", 2002);
        List<Book> books = newArrayList(book1, book2);
        insertValues(books);
    }

    @After
    public void tearDown() throws Exception {
        clearRepository();
        connection.close();
    }

    @Test
    public void allBooksFetched() throws Exception {
        List<Book> expected = bookRepository.selectAllBooks();
        assertThat(expected.size(), is(2));
    }

    @Test
    public void bookFetchedByName() throws Exception {
        String bookName = "Eragon";
        List<Book> result = bookRepository.selectBookByName(bookName);
        String actual = result.get(0).bookName;
        assertThat(actual, is(bookName));
    }

    @Test
    public void bookAdded() throws Exception {
        Book newBook = new Book("Amber","Roger Zelazny", 1960);
        bookRepository.addBook(newBook);
        List<Book> allBooks = bookRepository.selectAllBooks();
        int tableRows = allBooks.size();
        assertThat(tableRows, is(3));
    }

    @Test
    public void bookUpdated() throws Exception {
        Book updatedBook = new Book("The Name of the Wind", "Patrick Rothfuss", 2005);
        bookRepository.updateBook(updatedBook);
        List<Book> changedBook = bookRepository.selectBookByName(updatedBook.bookName);
        assertThat(updatedBook, is(equalTo(changedBook.get(0))));
    }

    @Test
    public void bookDeleted() throws Exception {
        Book book = new Book("Eragon", "Christopher Paulini", 2002);
        bookRepository.deleteBook(book);
        List<Book> allBooks = bookRepository.selectAllBooks();
        int tableRows = allBooks.size();
        assertThat(tableRows, is(1));
    }

    @Test
    public void booksAdded() throws Exception {
        Book book1 = new Book("TestName1", "TestAuthor1", 2010);
        Book book2 = new Book("TestName2", "TestAuthor2", 2011);
        List<Book> books = newArrayList(book1, book2);
        insertValues(books);
        List<Book> allBooks = bookRepository.selectAllBooks();
        int tableRows = allBooks.size();
        assertThat(tableRows,is(4));
    }

    public Connection getConnection() throws SQLException {
        String connectionString = "jdbc:postgresql://localhost:5432/";
        return connection = DriverManager.getConnection(connectionString, "postgres", "postgres");
    }

    public void createRepository() throws SQLException {
        Statement statement = connection.createStatement();
        String createDb = "CREATE DATABASE books_test";
        String tableCreateQuery = "CREATE TABLE books_test (" +
                "book_id SERIAL NOT NULL," +
                "book_name varchar(50)," +
                "author varchar(50)," +
                "year int" +
                ")";
        statement.execute(createDb);
        statement.execute(tableCreateQuery);
    }

    public void clearRepository() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("TRUNCATE books_test RESTART IDENTITY");
        statement.execute("DROP TABLE books_test");
        statement.execute("DROP DATABASE books_test");
    }

    public void insertValues(List<Book> books) throws SQLException {
        String insertQuery = "INSERT INTO books_test(book_name, author, year) VALUES (?,?,?)";
        PreparedStatement prepStatement = connection.prepareStatement(insertQuery);
        for (Book book : books) {
            prepStatement.setString(1,book.bookName);
            prepStatement.setString(2,book.author);
            prepStatement.setInt(3, book.year);
            prepStatement.executeUpdate();
        }
    }
}
