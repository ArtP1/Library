import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    // Reader(int cardNumber, String name, String phone)
    // Book(String isbn, String title, String subject, int pageCount, String author, LocalDate dueDate)
    // Shelf(int shelfNumber, String subject)

    Library testLibrary = null;
    List<Reader> testReaders;
    Reader testReader = null;
    Reader testReader2 = null;
    Reader testReader3 = null;
    Book testBook = null;
    Book testBook2 = null;



    @BeforeEach
    void setUp() {
        testLibrary = new Library("csumb");
        testReader = new Reader(1, "Jaime Palacios", "(831)-123-4567");
        testReader2 = new Reader(1, "Jaime Vasquez", "(831)-789-1234");
        testReader3 = new Reader(2, "Patricia Maravillo", "(831)-269-1234");
        testReaders = new ArrayList<>();
        testBook = new Book("9780345813925", "The Silent Patient", "Fiction", 350,"Alex Michaelides", LocalDate.now());
        testBook2 = new Book("9780123456789", "Introduction to Computer Science", "Computer Science", 400 ,"John Smith", LocalDate.now());
    }

    @AfterEach
    void tearDown() {
        testLibrary = null;
        testReader = null;
        testBook = null;
    }

    @Test
     void constructorTest() {
        Library testLibrary2 = null;
        assertNull(testLibrary2);
        testLibrary2 = testLibrary;
        assertNotNull(testLibrary2);
        assertEquals(testLibrary2, testLibrary);
        System.out.println("Constructor Test: SUCCESS\n");
    }
    @Test
    void removeReaderTest() {
        testLibrary.addReader(testReader);
        testLibrary.addShelf("Sci-Fi");
        assertNotNull(testLibrary.getReaderByCard(testReader.getCardNumber()));
        Code code = testLibrary.removeReader(testReader);
        assertNull(testLibrary.getReaderByCard(testReader.getCardNumber()));
        System.out.println("Remove Reader Test: SUCCESS\n");
    }

    @Test
    void addReaderTest() {
        testLibrary.addReader(testReader);
        testReader.addBook(testBook);
        assertEquals(1, testLibrary.listReaders());
        assertEquals(Code.READER_ALREADY_EXISTS_ERROR, testLibrary.addReader(testReader));
        assertEquals(Code.READER_CARD_NUMBER_ERROR, testLibrary.addReader(testReader2));
        System.out.println("Add Reader Test: SUCCESS\n");
    }

//  returnBook(Reader reader, Book book)
//  addBook(Book new_bk)

    @Test
    void listShelvesTest() {
        testLibrary.addShelf("Crime");
        testLibrary.addShelf("Computer Science");
        testLibrary.listShelves(false);
    }

    @Test
    void checkOutBook() {
        testLibrary.addShelf("Fiction");
        testLibrary.addShelf("Computer Science");
        testLibrary.addBook(testBook);
        testLibrary.addBook(testBook2);

        testLibrary.addReader(testReader);
        assertNotEquals(Code.SUCCESS, testLibrary.checkOutBook(testReader2, testBook));

        assertEquals(Code.SUCCESS, testLibrary.checkOutBook(testReader, testBook));
        testLibrary.getReaderByCard(1);
        testLibrary.addReader(testReader2);
        testLibrary.addReader(testReader3);
        testLibrary.listReaders(false);
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, testLibrary.checkOutBook(testReader3, testBook));

    }

}