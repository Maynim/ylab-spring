package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookRepository extends BaseRepository<BookEntity> {

    public BookRepository() {
        super("book");
    }

}
