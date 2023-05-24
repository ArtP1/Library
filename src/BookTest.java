import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book testBook = null;
    @BeforeEach
    void setUp() {
        testBook = new Book("9780345813925", "The Silent Patient", "Psychological Thriller", 1137,"Alex Michaelides", LocalDate.now());
    }

    @AfterEach
    void tearDown() {
        testBook = null;
    }

    @Test
    void testConstructor() {
        Book testBook2 = null;
        assertNull(testBook2);
        testBook2 = testBook;
        assertNotNull(testBook2);
        assertEquals(testBook2, testBook);
        System.out.println("Constructor Test: SUCCESS");

    }


    @Test
    void testToString() {
        assertEquals("The Silent Patient by Alex Michaelides ISBN: 9780345813925", testBook.toString());
        System.out.println("toString Test: SUCCESS");
    }

    @Test
    void testGettersSetters() {
        testBook.setIsbn("978-3-16-148410-0");
        testBook.setTitle("Random");
        testBook.setSubject("Math");
        testBook.setPageCount(1000);
        testBook.setAuthor("Jaime Palacios");
        testBook.setDueDate(LocalDate.of(2023, 4, 10));


        assertEquals("Random", testBook.getTitle());
        assertEquals("Math", testBook.getSubject());
        assertEquals(1000, testBook.getPageCount());
        assertEquals("Jaime Palacios", testBook.getAuthor());
        assertEquals(LocalDate.of(2023, 4, 10), testBook.getDueDate());

        System.out.println("Getters & Setters Test: SUCCESS");
    }
}