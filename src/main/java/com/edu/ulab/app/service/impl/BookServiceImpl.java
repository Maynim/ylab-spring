package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.template.BookTemplateRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookTemplateRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookTemplateRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);

        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);

        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);

        Book updatedBook = bookRepository.save(book);
        log.info("Updated book: {}", updatedBook);

        return bookMapper.bookToBookDto(updatedBook);
    }

    @Override
    public Optional<BookDto> getBookById(Long id) {
        return Optional.of(bookRepository.findById(id)
                .map(bookEntity -> {
                    BookDto bookMappedDto = bookMapper.bookToBookDto(bookEntity);
                    log.info("Mapped book: {}", bookMappedDto);

                    return bookMappedDto;
                })
                .orElseThrow(() -> new NotFoundException("Book not found")));
    }

    @Override
    public void deleteBookById(Long id) {
        Optional<Book> maybeBook = bookRepository.findById(id);
        log.info("Book for delete: {}", maybeBook);

        maybeBook.ifPresentOrElse(
            bookRepository::delete,
            () -> {
                throw new NotFoundException("User not found");
            }
        );
    }

    @Override
    public List<BookDto> getBooksByPersonId(Long personId) {
        List<Book> bookList = bookRepository.findAllByPersonId(personId);
        log.info("Received list of books: {}", bookList);

        return bookList.stream()
            .map(bookMapper::bookToBookDto)
            .toList();
    }

}
