import java.util.ArrayList;
import java.util.List;

public class Reader {
    public static final int CARD_NUMBER_ = 0;
    public static final int NAME_ = 1;
    public static final int PHONE_ = 2;
    public static final int BOOK_COUNT_ = 3;
    public static final int BOOK_START_ = 4;

    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;
        this.books = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name + "(#" + cardNumber + ") has checked out " + books;
    }

    public Code addBook(Book new_bk) {
        if(books.contains(new_bk)) {
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        }

        books.add(new_bk);

        return Code.SUCCESS;
    }

    public Code removeBook(Book target_bk) {
        boolean containsBook = hasBook(target_bk);

        if(containsBook) {
            books.remove(target_bk);
            return Code.SUCCESS;
        } else if(!containsBook) {
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        } else {
            return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
        }

    }

    public boolean hasBook(Book target_bk) {
        return books.contains(target_bk);
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public int getBookCount() {
        return this.books.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;

        if (cardNumber != reader.cardNumber || !name.equals(reader.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = cardNumber;
        result = 31 * result + name.hashCode();
        result = 31 * result + phone.hashCode();
        return result;
    }
}
