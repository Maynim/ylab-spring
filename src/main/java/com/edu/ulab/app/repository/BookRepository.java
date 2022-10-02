package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findById(long id);

    @Query("SELECT b FROM Book b WHERE b.person.id = :id")
    List<Book> findAllByPersonId(long id);
}
