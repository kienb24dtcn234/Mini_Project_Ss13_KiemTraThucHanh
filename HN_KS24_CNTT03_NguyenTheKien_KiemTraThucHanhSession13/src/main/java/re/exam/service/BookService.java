package re.exam.service;

import re.exam.entity.Book;
import re.exam.exception.BookNotFoundException;
import re.exam.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        log.debug("Lấy danh sách tất cả sách");
        List<Book> books = bookRepository.findAll();
        log.info("Trả về {} cuốn sách", books.size());
        return books;
    }

    public Book getBookById(Long id) {
        log.debug("Tìm sách với ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Không tìm thấy sách với ID: {}", id);
                    return new BookNotFoundException(id);
                });
    }

    public Book createBook(Book book) {
        log.debug("Tạo sách mới: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Đã tạo sách thành công với ID: {}", savedBook.getId());
        return savedBook;
    }

    public Book updateBook(Long id, Book bookDetails) {
        log.debug("Cập nhật toàn bộ sách ID: {} với dữ liệu: {}", id, bookDetails);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Không tìm thấy sách với ID: {} để cập nhật", id);
                    return new BookNotFoundException(id);
                });

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setCategory(bookDetails.getCategory());
        book.setQuantity(bookDetails.getQuantity());

        Book updatedBook = bookRepository.save(book);
        log.info("Đã cập nhật toàn bộ sách ID: {} thành công", id);
        return updatedBook;
    }

    public Book patchBook(Long id, Book bookDetails) {
        log.debug("Cập nhật một phần sách ID: {} với dữ liệu: {}", id, bookDetails);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Không tìm thấy sách với ID: {} để cập nhật một phần", id);
                    return new BookNotFoundException(id);
                });

        if (bookDetails.getTitle() != null) {
            book.setTitle(bookDetails.getTitle());
        }
        if (bookDetails.getAuthor() != null) {
            book.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getCategory() != null) {
            book.setCategory(bookDetails.getCategory());
        }
        if (bookDetails.getQuantity() != null) {
            book.setQuantity(bookDetails.getQuantity());
        }

        Book patchedBook = bookRepository.save(book);
        log.info("Đã cập nhật một phần sách ID: {} thành công", id);
        return patchedBook;
    }

    public void deleteBook(Long id) {
        log.debug("Xóa sách với ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Không tìm thấy sách với ID: {} để xóa", id);
                    return new BookNotFoundException(id);
                });
        bookRepository.delete(book);
        log.info("Đã xóa sách ID: {} thành công", id);
    }
}
