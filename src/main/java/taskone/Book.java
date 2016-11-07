package taskone;

import com.google.common.base.Objects;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public class Book {
    public final String bookName;
    public final String author;
    public final int year;

    public Book(String bookName, String author, int year) {
        this.bookName = bookName;
        this.author = author;
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equal(bookName, book.bookName) &&
                Objects.equal(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(bookName, author, year);
    }
}
