package re.exam.service;

import re.exam.entity.Book;
import re.exam.exception.BookNotFoundException;
import re.exam.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("getAllBooks_returnList")
    void getAllBooks_returnList() {
        Book book1 = new Book(1L, "Java Core", "Nguyen Van A", "Programming", 10);
        Book book2 = new Book(2L, "Spring Boot", "Tran Van B", "Framework", 5);
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getBookById_found")
    void getBookById_found() {
        Book book = new Book(1L, "Java Core", "Nguyen Van A", "Programming", 10);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Java Core", result.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getBookById_notFound")
    void getBookById_notFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(99L));
        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("createBook_success")
    void createBook_success() {
        Book book = new Book(null, "New Book", "Author", "Category", 3);
        Book savedBook = new Book(1L, "New Book", "Author", "Category", 3);
        when(bookRepository.save(book)).thenReturn(savedBook);

        Book result = bookService.createBook(book);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("deleteBook_notFound")
    void deleteBook_notFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(99L));
        verify(bookRepository, times(1)).findById(99L);
        verify(bookRepository, never()).delete(any(Book.class));
    }
}
