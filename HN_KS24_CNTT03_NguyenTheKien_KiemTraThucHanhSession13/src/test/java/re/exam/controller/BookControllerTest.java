package re.exam.controller;

import re.exam.entity.Book;
import re.exam.exception.BookNotFoundException;
import re.exam.exception.GlobalExceptionHandler;
import re.exam.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("GET /api/books → 200 OK và danh sách JSON")
    void getAllBooks_returns200() throws Exception {
        Book book1 = new Book(1L, "Java Core", "Nguyen Van A", "Programming", 10);
        Book book2 = new Book(2L, "Spring Boot", "Tran Van B", "Framework", 5);
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Java Core"))
                .andExpect(jsonPath("$[1].title").value("Spring Boot"));
    }

    @Test
    @DisplayName("GET /api/books/{id} khi tìm thấy → 200 OK")
    void getBookById_found_returns200() throws Exception {
        Book book = new Book(1L, "Java Core", "Nguyen Van A", "Programming", 10);
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Core"))
                .andExpect(jsonPath("$.author").value("Nguyen Van A"));
    }

    @Test
    @DisplayName("GET /api/books/{id} khi không tìm thấy → 404 Not Found")
    void getBookById_notFound_returns404() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Không tìm thấy sách với ID: 99"));
    }

    @Test
    @DisplayName("POST /api/books → 201 Created")
    void createBook_returns201() throws Exception {
        Book book = new Book(1L, "New Book", "Author", "Category", 3);
        when(bookService.createBook(any(Book.class))).thenReturn(book);

        String requestBody = """
                {
                    "title": "New Book",
                    "author": "Author",
                    "category": "Category",
                    "quantity": 3
                }
                """;

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"));
    }
}
