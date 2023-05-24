import java.util.HashMap;

public class Shelf {
    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;

    private int shelfNumber;
    private String subject;
    private HashMap<Book, Integer> books;

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        this.books = new HashMap<>();
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    public int getBookCount(Book target_bk) {
        Integer count = books.get(target_bk);

        if (count != null && count >= 0) {
            return count;
        } else {
            return -1;
        }
    }


    public Code addBook(Book new_bk) {
        if(books.containsKey(new_bk)) {
            int count = getBookCount(new_bk);
            books.put(new_bk, count + 1);
            System.out.println("Incremented book count of: " + new_bk.toString());
            return Code.SUCCESS;
        }

        if(new_bk.getSubject().equals(subject)) {
            books.put(new_bk, 1);
            System.out.println(new_bk.toString() + " added to shelf " + this.toString());
            return Code.SUCCESS;
        } else {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    public Code removeBook(Book target_bk) {
        boolean containsBook = books.containsKey(target_bk);

        if(!containsBook) {
            System.out.println(target_bk.getTitle() + " is not on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if(getBookCount(target_bk) == 0) {
            System.out.println("No copies of " + target_bk.getTitle() + " remain on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else {
            int count = getBookCount(target_bk);
            books.put(target_bk, count - 1);
            System.out.println(target_bk.getTitle() + " successfully removed from shelf " + subject);
            return Code.SUCCESS;
        }
    }

    public String listBooks() {
        StringBuilder sb = new StringBuilder();
        sb.append(books.size()).append(" books on shelf: ").append(shelfNumber).append(" : ").append(subject).append("\n");

        for(Book book : books.keySet()) {
            int count = getBookCount(book);
            sb.append(book.toString()).append(" ").append(count).append("\n");
        }

        return sb.toString();
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (shelfNumber != shelf.shelfNumber || !subject.equals(shelf.subject)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = shelfNumber;
        result = 31 * result + subject.hashCode();
        return result;
    }
}
