import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Library {
    public static final int LENDING_LIMIT = 5;

    private String name;
    private static int libraryCard;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;
    private HashMap<Book, Integer> books;


    public Library(String name) {
        this.name = name;
        this.shelves = new HashMap<>();
        libraryCard = 0;
        this.readers = new ArrayList<>();
        this.books = new HashMap<>();
    }

    public Code init(String filename) {
        File file = new File(filename);
        Scanner scan;

        try {
            scan = new Scanner(file);
        } catch(FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }

        int bookCount = convertInt(scan.nextLine(), Code.BOOK_COUNT_ERROR);
        if (bookCount < 0) {
            return errorCode(bookCount);
        }
        initBooks(bookCount, scan);
        listBooks();

        int shelfCount = convertInt(scan.nextLine(), Code.SHELF_NUMBER_PARSE_ERROR);
        if (shelfCount < 0) {
            return errorCode(shelfCount);
        }
        initShelves(shelfCount, scan);

        int readerCount = convertInt(scan.nextLine(), Code.READER_COUNT_ERROR);
        if (readerCount < 0) {
            return errorCode(readerCount);
        }
        initReader(readerCount, scan);

        return Code.SUCCESS;
    }

    private Code initBooks(int bookCount, Scanner scan) {
        if(bookCount < 1) {
            return Code.LIBRARY_ERROR;
        }

        int counter = 0;
        while(scan.hasNextLine() && counter < bookCount) {

            String fileLine = scan.nextLine();
            String[] bookData = fileLine.split(",");

            String pageCountCsv = bookData[Book.PAGE_COUNT_];
            String dueDateCsv = bookData[Book.DUE_DATE_];
            String titleCsv = bookData[Book.TITLE_];
            String isbnCsv = bookData[Book.ISBN_];
            String subjectCsv = bookData[Book.SUBJECT_];
            String authorCsv = bookData[Book.AUTHOR_];

            LocalDate dateConversionErrorCsv = convertDate(dueDateCsv, Code.DATE_CONVERSION_ERROR);
            int pageCountErrorCsv = convertInt(pageCountCsv, Code.PAGE_COUNT_ERROR);

            if (pageCountErrorCsv <= 0) {
                return Code.PAGE_COUNT_ERROR;
            } else if(dateConversionErrorCsv == null) {
                return Code.DATE_CONVERSION_ERROR;
            }

            Book book = new Book(isbnCsv, titleCsv, subjectCsv, pageCountErrorCsv, authorCsv, dateConversionErrorCsv);
            addBook(book);
            counter++;
        }
        return Code.SUCCESS;
    }

    private Code initShelves(int shelfQuantity, Scanner scan) {
        if (shelfQuantity < 1) {
            return Code.SHELF_COUNT_ERROR;
        }

        System.out.println("\nparsing " + shelfQuantity + " shelves");
        int counter = 0;
        while(scan.hasNextLine() && counter < shelfQuantity) {
            String fileLine = scan.nextLine();
            String[] shelfData = fileLine.split(",");
            String shelfNumCSV = shelfData[Shelf.SHELF_NUMBER_];
            String subjectCSV = shelfData[Shelf.SUBJECT_];

            int intShelfNumCSV = convertInt(shelfNumCSV, Code.SHELF_NUMBER_PARSE_ERROR);
            if (intShelfNumCSV <= 0) {
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }

            Shelf shelf = new Shelf(intShelfNumCSV,subjectCSV);
            addShelf(shelf);

            counter++;
        }

        if(shelves.size() != shelfQuantity) {
            System.out.println("Number of shelves doesn't match expected");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }

        return Code.SUCCESS;
    }

    private Code initReader(int readerCount, Scanner scan) {

        if(readerCount <= 0) {
            return Code.READER_COUNT_ERROR;
        }

        int counter = 0;
        while(scan.hasNextLine() && counter < readerCount) {
            String fileLine = scan.nextLine();
            String[] readerData = fileLine.split(",");
            String cardNumCSV = readerData[Reader.CARD_NUMBER_];
            String phoneCSV = readerData[Reader.PHONE_];

            String nameCSV = readerData[Reader.NAME_];
            String bookcountCSV = readerData[Reader.BOOK_COUNT_];

            int intBCount = convertInt(bookcountCSV, Code.BOOK_COUNT_ERROR);
            int intcardNum = convertInt(cardNumCSV, Code.SHELF_NUMBER_PARSE_ERROR);

            Reader read = new Reader(intcardNum, nameCSV, phoneCSV);
            addReader(read);

            for (int i = Reader.BOOK_START_; i < (intBCount*2+4); i+=2) {
                Book book = getBookByISBN(readerData[i]);
                if(book == null) {
                    System.out.println("ERROR");
                }

                String date = readerData[i+1];
                LocalDate dueDate = convertDate(date, Code.DATE_CONVERSION_ERROR);
                book.setDueDate(dueDate);
                checkOutBook(read, book);
            }
            counter++;
        }

        return Code.SUCCESS;
    }

    

    public static int convertInt(String recordCountString, Code code) {
        try {
            int number = Integer.parseInt(recordCountString);
            return number;
        } catch (NumberFormatException e) {
            System.out.println("Value which caused the error: " + recordCountString);
            System.out.println("Error message: " + code.getMessage());

            return code.getCode();
        }
    }

    public static LocalDate convertDate(String date, Code errorCode) {

        if (date.equals("0000")) {
            return LocalDate.of(1970, 1, 1);
        }

        String[] dateValues = date.split("-");
        int[] dates = new int[dateValues.length];

        for (int i = 0; i < dateValues.length; i++) { //loop to parse string values
            dates[i] = Integer.parseInt(dateValues[i]);
        }

        if (dates.length < 3) {
            System.out.println("ERROR: " + errorCode.getMessage() + ", could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");

            return LocalDate.of(1970, 1, 1);
        }

        for (int intDateValue : dates) {
            if (intDateValue < 0) {
                System.out.println("Error converting date: Day " + dates[2]);
                System.out.println("Error converting date: Month " + dates[1]);
                System.out.println("Error converting date: Year " + dates[0]);
                System.out.println("Using default date (01-jan-1970)");

                return LocalDate.of(1970, 1, 1);
            }
        }

        return LocalDate.of(dates[0], dates[1], dates[2]);
    }

    public Code removeReader(Reader target_reader) {
        boolean containsReader = readers.contains(target_reader);

        if(containsReader) {
            if(target_reader.getBookCount() > 0){
                System.out.println(target_reader.getName() + " must return all books!");
                return Code.READER_STILL_HAS_BOOKS_ERROR;
            } else {
                readers.remove(target_reader);
                return Code.SUCCESS;
            }
        }


        System.out.println(target_reader.getName() + "\n" + "is not part of this Library");
        return Code.READER_NOT_IN_LIBRARY_ERROR;
    }

    public Code addReader(Reader new_reader) {
        boolean containsReader = readers.contains(new_reader);

        if(containsReader) {
            System.out.println(new_reader.getName() + " already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }

        for(Reader reader : readers) {
            if(reader.getCardNumber() == new_reader.getCardNumber()) {
                System.out.println(reader.getName() + " and " + new_reader.getName() + " have the same card number!");
                return Code.READER_CARD_NUMBER_ERROR;
            }
        }

        readers.add(new_reader);
        System.out.println(new_reader.getName() + " added to the library!");

        if(new_reader.getCardNumber() > libraryCard) {
            libraryCard = new_reader.getCardNumber();
        }

        return Code.SUCCESS;
    }

    public Reader getReaderByCard(int cardNum) {
        for(Reader reader : readers) {
            if(reader.getCardNumber() == cardNum) {
                System.out.println(reader.toString());
                return reader;
            }
        }

        System.out.println("Could not find a reader with card #[" + cardNum + "]");
        return null;
    }

    public int listReaders(boolean showBooks) {
        StringBuilder sb = new StringBuilder();

        for(Reader reader : readers) {
            sb.append(reader.toString());
            List<Book> readerBooks = reader.getBooks();

            if (readerBooks.isEmpty()) {
                sb.append("no books.");
            } else {
                sb.append("the following books:\n");
                for (Book book : readerBooks) {
                    sb.append("- ").append(book.toString()).append("\n");
                }
            }
        }

        System.out.println(sb.toString());

//        if(showBooks && !books.isEmpty()) {
//            for(Reader reader : readers) {
//                System.out.println(reader.getName() + "(#" + reader.getCardNumber() + ") has the following books:");
//                List<Book> readerBooks = reader.getBooks();
//                System.out.println("[");
//                for(Book book : readerBooks) {
//                    System.out.println(book.toString());
//                }
//                System.out.println("]");
//            }
//        } else {
//            for(Reader reader : readers) {
//                System.out.println(reader.toString());
//            }
//        }

        return readers.size();
    }

    public int listReaders() {
        for(Reader reader : readers) {
            System.out.println(reader.toString());
        }

        return readers.size();
    }

    public Shelf getShelf(String shelfSubject) {
        boolean containsShelf = shelves.containsKey(shelfSubject);

        if(!containsShelf) {
            System.out.println("No shelf for " + shelfSubject + " books");
            return null;
        }

        return shelves.get(shelfSubject);
    }
    public Shelf getShelf(Integer shelfNumber) {
        for(String subject : shelves.keySet()) {
            Shelf shelf = shelves.get(subject);

            if(shelf.getShelfNumber() == shelfNumber) {
                return shelf;
            }
        }
        System.out.println("No shelf number " + shelfNumber + " found");
        return null;
    }

    public Code addShelf(String shelfSubject) {
        Shelf shelf = new Shelf(shelves.size() + 1, shelfSubject);
        return addShelf(shelf);
    }

    public Code addShelf(Shelf shelf) {
        String shelfSubject =  shelf.getSubject();
        boolean containsShelf = shelves.containsKey(shelfSubject);

        if(containsShelf) {
            System.out.println("ERROR: Shelf already exists " + shelf.toString());
            return Code.SHELF_EXISTS_ERROR;
        }

        System.out.println(shelf.getSubject() + " shelf has been added to " + name + " library");
        shelves.put(shelfSubject, shelf);
        return  Code.SUCCESS;
    }

    public Code listShelves(boolean showBooks) {
        Shelf shelf;
        StringBuilder output = new StringBuilder();

        if (showBooks) {
            for (String subject : shelves.keySet()) {
                shelf = shelves.get(subject);
                output.append("Books on ").append(subject).append(" shelf:\n");
                output.append("- ").append(shelf.listBooks()).append("\n");
            }
        } else {
            output.append("Shelf details:\n");
            for (String subject : shelves.keySet()) {
                shelf = shelves.get(subject);
                output.append("- ").append(subject).append(": ").append(shelf.toString()).append("\n");
            }
        }

        System.out.println(output.toString());
        return Code.SUCCESS;
    }


    public Book getBookByISBN(String isbn) {
        for(Book book : books.keySet()) {
            if(book.getIsbn().equals(isbn)) {
                return book;
            }
        }

        System.out.println("ERROR: Could not find a book with ISBN: " + isbn);
        return null;
    }

    public Code checkOutBook(Reader reader, Book book) {
        boolean containsReader = readers.contains(reader);
        boolean containsBook = books.containsKey(book);
        String bookSubject = book.getSubject();


        if(!containsReader) {
            System.out.println(reader.getName() + " doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }

        if(reader.getBookCount() > LENDING_LIMIT) {
            System.out.println(reader.getName() + " has reached the lending limit, (" + LENDING_LIMIT + ")");
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }

        if(!containsBook) {
            System.out.println("ERROR: could not find " + book.toString());
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Shelf shelf = shelves.get(bookSubject);

        if(shelf == null) {
            System.out.println("No shelf for " + bookSubject + " books");
        } else if(shelf.getBookCount(book) == 0) {
            System.out.println("ERROR: no copies of " + book.toString() + " remain");
        }

        Code code = reader.addBook(book);

        if(code != Code.SUCCESS) {
            System.out.println("Couldn't checkout " + book.toString());
            return code;
        }

        Code code2 = shelf.removeBook(book);

        if(code2 == Code.SUCCESS) {
            System.out.println(book.toString() + " checked out successfully");
            return code2;
        }

        return code2;

    }

    public int listBooks() {

        for(Book book : books.keySet()) {
            int count = books.get(book);
            System.out.println(count + " copies of " + book.toString());
        }
        return books.size();
    }

    private Code addBookToShelf(Book book, Shelf shelf) {
        Code code = returnBook(book);

        if(code == Code.SUCCESS) {
            return code;
        }

        if(shelf.getSubject() != book.getSubject()) {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }

        Code code2 = shelf.addBook(book);

        if(code2 == Code.SUCCESS) {
            System.out.println(book.toString() + " added to shelf");
            return Code.SUCCESS;
        } else {
            System.out.println("Could not add " + book.toString() + " to shelf" );
            return code2;
        }
    }

    public Code returnBook(Reader reader, Book book) {
        if(!reader.hasBook(book)) {
            System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }

        if(!books.containsKey(book)) {
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Code code = reader.removeBook(book);

        if(code == Code.SUCCESS) {
            returnBook(book);
            return code;
        } else {
            System.out.println("Could not return " + book.toString());
            return code;
        }

    }

    public Code returnBook(Book book) {
        String bookSubject = book.getSubject();

        Shelf shelf = shelves.get(bookSubject);

        if(shelf != null) {
            System.out.println("No shelf for " + book.toString());
            return Code.SHELF_EXISTS_ERROR;
        }

        shelf.addBook(book);

        return Code.SUCCESS;
    }

    public Code addBook(Book new_bk) {
        boolean containsBook = books.containsKey(new_bk);
        String bookSubject = new_bk.getSubject();

        if(containsBook) {
            int count = books.get(new_bk);
            books.put(new_bk, count + 1);
            System.out.println(count + " copies of " + new_bk.getTitle() + " in the stacks");
        } else  {
            books.put(new_bk, 1);
            System.out.println(new_bk.getTitle() + " added to the stacks");
        }

        if(shelves.containsKey(bookSubject)) {
            Shelf shelf = shelves.get(bookSubject);
            shelf.addBook(new_bk);
            return Code.SUCCESS;
        } else {
            System.out.println("No shelf for " + bookSubject + " books");
            return Code.SHELF_EXISTS_ERROR;
        }

    }
    private Code errorCode(int tag) {
        for (Code code: Code.values()) {
            if (code.getCode() == tag) {
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }

}
