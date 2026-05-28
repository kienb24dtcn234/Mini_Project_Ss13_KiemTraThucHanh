package re.exam.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Không tìm thấy sách với ID: " + id);
    }
}
