public enum Code {
    /**
     * The following codes are returned based on condition in the library project.
     */
    SUCCESS(0, "Transaction was a success"),
    FILE_NOT_FOUND_ERROR(-1, "Could not the file"),
    BOOK_COUNT_ERROR(-2, "Incorrect book count"),
    LIBRARY_ERROR(-3, "problem with the library"),
    LIBRARY_OUT_OF_BOOKS_ERROR(-31, "No books in library"),
    LIBRARY_OUT_OF_SHELVES_ERROR(-32, "No shelves for books"),
    BOOK_ALREADY_CHECKED_OUT_ERROR(-21, "Book already checked out error"),
    BOOK_LIMIT_REACHED_ERROR(-22, "Book limit reached"),
    BOOK_NOT_IN_INVENTORY_ERROR(-23, "book not in stacks or library"),
    READER_COUNT_ERROR(-4, "Reader Count Error"),
    READER_CARD_NUMBER_ERROR(-41, "Reader Card number error"),
    READER_PHONE_NUMBER_ERROR(-43,"Reader Phone number error"),
    READER_NOT_IN_LIBRARY_ERROR(-44,"Reader Does not exists in library"),
    READER_DOESNT_HAVE_BOOK_ERROR(-45, "Reader doesn't have book error"),
    READER_COULD_NOT_REMOVE_BOOK_ERROR(-46, "Reader won't let go of the book"),
    READER_ALREADY_EXISTS_ERROR(-47, "Reader already exists!"),
    READER_STILL_HAS_BOOKS_ERROR(-48, "Must return all books."),
    SHELF_COUNT_ERROR(-6,"Shelf count error"),
    SHELF_NUMBER_PARSE_ERROR(-61,"Shelf Number parse error"),
    SHELF_EXISTS_ERROR(-62,"shelf exists error"),
    SHELF_SUBJECT_MISMATCH_ERROR(-63,"Subjects do not match"),
    PAGE_COUNT_ERROR(-8,"Page count error"),
    DUE_DATE_ERROR(-10,"Due date error"),
    DATE_CONVERSION_ERROR(-101, "Date conversion Error"),
    NOT_IMPLEMENTED_ERROR(-99,"Not yet implemented error"),
    UNKNOWN_ERROR(-999, "Unknown Error");
    private final int code;
    private final String message;
    Code(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}