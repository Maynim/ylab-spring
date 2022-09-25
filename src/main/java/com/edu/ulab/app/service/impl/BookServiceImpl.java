package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        log.info("Mapped book: {}", bookEntity);

        BookEntity savedBook = bookRepository.save(bookEntity);
        log.info("Saved book: {}", savedBook);

        return  bookMapper.bookEntityToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);
        log.info("Mapped book: {}", bookEntity);

        boolean update = bookRepository.update(bookEntity);
        log.info("Update successful: {}", update);
        if (!update) {
            throw new NotFoundException("Book not found");
        }

        return bookDto;
    }

    @Override
    public Optional<BookDto> getBookById(Long id) {
        return Optional.of(bookRepository.findById(id)
                .map(bookEntity -> {
                    BookDto bookMappedDto = bookMapper.bookEntityToBookDto(bookEntity);
                    log.info("Mapped book: {}", bookMappedDto);

                    return bookMappedDto;
                })
                .orElseThrow(() -> new NotFoundException("Book not found")));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.delete(id);
    }
}
