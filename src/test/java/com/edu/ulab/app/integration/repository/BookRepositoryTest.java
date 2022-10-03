package com.edu.ulab.app.integration.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.repository.BookRepository;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
@RequiredArgsConstructor
@DisplayName("Интеграционное тестирование BookRepository.")
public class BookRepositoryTest {

    private final BookRepository bookRepository;
    private final EntityManager entityManager;

    // given
    private static final Person TEST_PERSON =
            Person.builder()
                    .id(1001L)
                    .title("reader")
                    .fullName("default user")
                    .email("email")
                    .age(55)
                    .build();

    private static final Book TEST_BOOK =
            Book.builder()
                    .id(2002L)
                    .person(TEST_PERSON)
                    .title("default book")
                    .author("author")
                    .pageCount(5500)
                    .build();

    private static final Integer COUNT_TEST_BOOK = 2;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу. Число insert - 1")
    @Test
    void saveBook_thenAssertDmlCount() {

        //Given
        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPerson(TEST_PERSON);

        //When
        Book result = bookRepository.save(book);
        entityManager.flush();

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");

        assertSelectCount(0);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Обновить книгу. Число select - 1, update - 1.")
    void updateBook_thenAssertDmlCount() {

        // given
        int expectedPageCount = 100;

        Book updateBook = Book.builder()
                .id(TEST_BOOK.getId())
                .person(TEST_PERSON)
                .title("Some title")
                .author("Some author")
                .pageCount(expectedPageCount)
                .build();

        // when
        Book savedBook = bookRepository.save(updateBook);
        entityManager.flush();

        // then
        assertThat(savedBook.getPageCount()).isEqualTo(expectedPageCount);

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Получить книгу. Число select - 1.")
    void getBook_thenAssertDmlCount() {

        // then
        Optional<Book> findBook = bookRepository.findById(TEST_BOOK.getId());

        // when
        assertEquals(TEST_BOOK, findBook.orElse(null));

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Получить все книги. Число select - 1.")
    void getAllBook_thenAssertDmlCount() {

        // then
        List<Book> bookList = bookRepository.findAll();

        // when
        assertEquals(COUNT_TEST_BOOK, bookList.size());

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Удалить книгу. Число select - 2, delete - 1.")
    void deleteBook_thenAssertDmlCount() {

        // then
        bookRepository.delete(TEST_BOOK);
        entityManager.flush();

        // when
        assertTrue(bookRepository.findById(TEST_BOOK.getId()).isEmpty());

        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    // TODO: 03.10.2022 fail tests
}
