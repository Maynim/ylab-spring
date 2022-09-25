package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;

import java.util.Optional;

public interface BookService {
    BookDto createBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto);

    Optional<BookDto> getBookById(Long id);

    void deleteBookById(Long id);
}
