package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookEntity createBook(BookEntity bookEntity) {
        return bookRepository.save(bookEntity);
    }

    @Override
    public BookEntity updateBook(BookEntity bookEntity) {
        return bookRepository.update(bookEntity);
    }

    @Override
    public BookEntity getBookById(Long id) {
        Optional<BookEntity> maybeBook = bookRepository.findById(id);

        return maybeBook.orElseThrow(() -> new NotFoundException("Book not found"));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.delete(id);
    }
}
