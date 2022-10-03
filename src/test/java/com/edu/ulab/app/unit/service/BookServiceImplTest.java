package com.edu.ulab.app.unit.service;

import com.edu.ulab.app.config.UnitTest;
import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link BookServiceImpl}.
 */
@UnitTest
@DisplayName("Тестирование функциональности BookService.")
public class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @InjectMocks
    BookServiceImpl bookService;

    private static final Long ID = 1L;

    private static final Person PERSON =
            Person.builder()
                    .id(1L)
                    .fullName("Test name")
                    .title("Test title")
                    .email("Test email")
                    .age(100)
                    .build();

    private static final BookDto INPUT_BOOK =
            BookDto.builder()
                    .personId(PERSON.getId())
                    .title("Test title")
                    .author("Test author")
                    .pageCount(101)
                    .build();

    private static final BookDto INPUT_BOOK_WITH_ID =
            BookDto.builder()
                    .id(ID)
                    .personId(PERSON.getId())
                    .title("Test title")
                    .author("Test author")
                    .pageCount(101)
                    .build();

    private static final Book BOOK =
            Book.builder()
                    .person(PERSON)
                    .title("Test title")
                    .author("Test author")
                    .pageCount(101)
                    .build();

    private static final Book BOOK_WITH_ID =
            Book.builder()
                    .id(ID)
                    .person(PERSON)
                    .title("Test title")
                    .author("Test author")
                    .pageCount(101)
                    .build();

    private static final Book FIND_BOOK =
            Book.builder()
                    .id(ID)
                    .person(PERSON)
                    .title("Test title")
                    .author("Test author")
                    .pageCount(101)
                    .build();

    private static final Book SAVED_BOOK =
            Book.builder()
                    .id(ID)
                    .person(PERSON)
                    .title("Test title")
                    .author("Test author")
                    .pageCount(101)
                    .build();

    private static final BookDto OUTPUT_BOOK =
            BookDto.builder()
                    .id(ID)
                    .personId(PERSON.getId())
                    .title("Test title")
                    .author("Test author")
                    .pageCount(101)
                    .build();

    @Test
    @DisplayName("Создать книгу. Должно пройти успешно.")
    void saveBook_Test() {

        //when
        when(bookMapper.bookDtoToBook(INPUT_BOOK)).thenReturn(BOOK);
        when(bookRepository.save(BOOK)).thenReturn(SAVED_BOOK);
        when(bookMapper.bookToBookDto(SAVED_BOOK)).thenReturn(OUTPUT_BOOK);

        //then
        BookDto bookDtoResult = bookService.createBook(INPUT_BOOK);
        assertEquals(ID, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновить книгу. Должно пройти успешно.")
    void updateBook_Test() {

        //when
        when(bookMapper.bookDtoToBook(INPUT_BOOK_WITH_ID)).thenReturn(BOOK_WITH_ID);
        when(bookRepository.findById(ID)).thenReturn(Optional.of(FIND_BOOK));
        when(bookRepository.save(FIND_BOOK)).thenReturn(SAVED_BOOK);
        when(bookMapper.bookToBookDto(SAVED_BOOK)).thenReturn(OUTPUT_BOOK);

        //then
        BookDto bookDtoResult = bookService.updateBook(INPUT_BOOK_WITH_ID);
        assertEquals(ID, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновить несуществующую книгу. Должна быть выброшена ошибка.")
    void updateNoNExistentBook_Test() {

        //when
        when(bookMapper.bookDtoToBook(INPUT_BOOK_WITH_ID)).thenReturn(BOOK_WITH_ID);
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());
        when(bookMapper.bookDtoToBook(INPUT_BOOK)).thenReturn(BOOK);

        //then
        assertThatThrownBy(() -> bookService.updateBook(INPUT_BOOK_WITH_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    @DisplayName("Получить книгу. Должно пройти успешно.")
    void getBook_Test() {

        //when
        when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        when(bookMapper.bookToBookDto(BOOK)).thenReturn(OUTPUT_BOOK);

        //then
        BookDto bookDtoResult = bookService.getBookById(ID);
        assertEquals(ID, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Получить несуществующую книгу. Должна быть выброшена ошибка.")
    void getNoNExistentBook_Test() {

        //when
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> bookService.getBookById(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found");
    }

    @Test
    @DisplayName("Удалить книгу. Должно пройти успешно.")
    void deleteBook_Test() {

        //when
        when(bookRepository.findById(ID)).thenReturn(Optional.of(BOOK));
        doNothing().when(bookRepository).delete(BOOK);

        //then
        assertDoesNotThrow(() -> bookService.deleteBookById(ID));
    }

    @Test
    @DisplayName("Удалить несуществующую книгу. Должна быть выброшена ошибка.")
    void deleteNoNExistentBook_Test() {

        //when
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> bookService.deleteBookById(ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book not found");
    }
}
