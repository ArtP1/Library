import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    Reader testReader = null;
    Book testBook = null;
    @BeforeEach
    void setUp() {
        testReader = new Reader(2, "Jaime Palacios", "(831) 123-4567");
        testBook = new Book("9780345813925", "The Silent Patient", "Psychological Thriller", 1137,"Alex Michaelides", LocalDate.now());
    }

    @AfterEach
    void tearDown() {
        testReader = null;
    }

    @Test
    void addBookTest() {
        assertEquals(testReader.addBook(testBook), Code.SUCCESS);
        assertNotEquals(testReader.addBook(testBook), Code.SUCCESS);
        assertEquals(testReader.addBook(testBook), Code.BOOK_ALREADY_CHECKED_OUT_ERROR);
    }

    @Test
    void getBookCountTest() {
        assertEquals(0, testReader.getBookCount());
        testReader.addBook(testBook);
        assertEquals(1, testReader.getBookCount());
        testReader.removeBook(testBook);
        assertEquals(0, testReader.getBookCount());
    }

    @Test
    void hasBook() {
        assertFalse(testReader.hasBook(testBook));
        testReader.addBook(testBook);
        assertTrue(testReader.hasBook(testBook));
    }

    @Test
    void removeBookTest() {
        assertEquals(testReader.removeBook(testBook), Code.READER_DOESNT_HAVE_BOOK_ERROR);
        testReader.addBook(testBook);
        assertEquals(testReader.removeBook(testBook), Code.SUCCESS);
    }
}