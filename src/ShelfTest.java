import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {
    Shelf testShelf = null;
    Book testBook = null;
    Book testBook2 = null;
    Book testBook3 = null;
    HashMap<Book, Integer> books;
    Random random = new Random();

    @BeforeEach
    void setUp() {
        testShelf = new Shelf(0, "Fiction");
        testBook = new Book("9780345813925", "The Silent Patient", "Fiction", 350,"Alex Michaelides", LocalDate.now());
        testBook2 = new Book("9780062678423", "Educated: A Memoir", "Fiction", 400 ,"Tara Westover", LocalDate.of(2023, 4, 10));
        testBook3 = new Book("9781982111014", "Where the Crawdads Sing", "sci-fi", 384  ,"Delia Owens", LocalDate.of(2023, 5, 10));
    }

    @AfterEach
    void tearDown() {
        testShelf = null;
        testBook = null;
        testBook2 = null;
        testBook3 = null;
    }

    @Test
    void constructorTest() {
        Shelf testShelf2 = null;
        assertNull(testShelf2);
        testShelf2 = testShelf;
        assertNotNull(testShelf2);
        assertEquals(testShelf2, testShelf);
        System.out.println("Constructor Test: SUCCESS");
    }

    @Test
    void gettersSettersTest() {
        testShelf.setShelfNumber(1);
        testShelf.setSubject("Technology");
        HashMap<Book, Integer> expectedBooks = new HashMap<>();
        expectedBooks.put(testBook, 1);
        expectedBooks.put(testBook2, 4);

        assertEquals(1, testShelf.getShelfNumber());
        assertEquals("Technology", testShelf.getSubject());
        assertEquals(expectedBooks, testShelf.getBooks());

        System.out.println("Getters & Setters Test: SUCCESS");
    }

    @Test
    void addBookTest() {
        assertEquals(Code.SUCCESS, testShelf.addBook(testBook));
        assertEquals(1, testShelf.getBookCount(testBook));
        assertEquals(Code.SUCCESS, testShelf.addBook(testBook2));
        assertEquals(1, testShelf.getBookCount(testBook2));
        assertEquals(Code.SHELF_SUBJECT_MISMATCH_ERROR, testShelf.addBook(testBook3));
        System.out.println("Add Book Test: SUCCESS");
    }

    @Test
    void removeBookTest() {
        assertEquals(Code.BOOK_NOT_IN_INVENTORY_ERROR, testShelf.removeBook(testBook3));
        assertEquals(Code.SUCCESS, testShelf.addBook(testBook));
        assertEquals(1, testShelf.getBookCount(testBook));
        assertEquals(Code.SUCCESS, testShelf.removeBook(testBook));
        assertEquals(0, testShelf.getBookCount(testBook));
    }

    @Test
    void getBookCountTest() {
        int n = random.nextInt(50) + 1;

        for(int i = 0; i < n; i++) {
            testShelf.addBook(testBook);
        }

        assertEquals(n, testShelf.getBookCount(testBook));
        System.out.println("1. " + n + " = " + testShelf.getBookCount(testBook));
        assertEquals(Code.SUCCESS, testShelf.removeBook(testBook));

        for(int j = 0; j < n - 1; j++) {
            testShelf.removeBook(testBook);
        }

        assertEquals(0, testShelf.getBookCount(testBook));
        System.out.println("2. " + 0 + " = " + testShelf.getBookCount(testBook));
        assertEquals(-1, testShelf.getBookCount(testBook3));
        System.out.println("3. " + -1 + " = " + testShelf.getBookCount(testBook3));
    }


    @Test
    void listBooksTest() {
        testShelf.addBook(testBook);
        testShelf.addBook(testBook2);
        System.out.println(testShelf.listBooks());
    }
}