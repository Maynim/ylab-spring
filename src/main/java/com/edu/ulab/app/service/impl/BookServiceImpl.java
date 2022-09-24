package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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

        BookEntity savedBook = bookRepository.save(bookEntity);

        return  bookMapper.bookEntityToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);

        boolean update = bookRepository.update(bookEntity);
        if (!update) {
            throw new NotFoundException("Book not found");
        }

        return bookDto;
    }

    @Override
    public Optional<BookDto> getBookById(Long id) {
        return Optional.of(bookRepository.findById(id)
                .map(bookMapper::bookEntityToBookDto)
                .orElseThrow(() -> new NotFoundException("Book not found")));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.delete(id);
    }

    @Override
    public List<BookDto> getBooksByUserId(Long id) {

        return null;
    }
}
