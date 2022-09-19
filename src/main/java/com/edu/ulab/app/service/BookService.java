package com.edu.ulab.app.service;

import com.edu.ulab.app.entity.BookEntity;

public interface BookService {
    BookEntity createBook(BookEntity bookEntity);

    BookEntity updateBook(BookEntity bookEntity);

    BookEntity getBookById(Long id);

    void deleteBookById(Long id);
}
