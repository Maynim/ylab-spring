package com.edu.ulab.app.repository.template;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BookTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String FIND_BY_ID_SQL = "SELECT * FROM book b WHERE b.id = ?";
    private static final String FIND_ALL_BY_PERSON_ID_SQL = "SELECT * FROM book b WHERE b.person_id = ?";
    private static final String UPDATE_SQL = "UPDATE book SET title = ?, author = ?, page_count = ?, person_id = ?" +
                                             "WHERE id = ?";
    private static final String SAVE_SQL = "INSERT INTO book(title, author, page_count, person_id) VALUES (?,?,?,?)";
    private static final String DELETE_SQL = "DELETE FROM book WHERE id = ?";

    public BookTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Book> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, new BookRowMapper(), id).stream().findAny();
    }

    public List<Book> findAllByPersonId(long personId) {
        return jdbcTemplate.query(FIND_ALL_BY_PERSON_ID_SQL, new BookRowMapper(), personId);
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SAVE_SQL, new String[]{"id"});
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    ps.setLong(3, book.getPageCount());
                    ps.setLong(4, book.getPerson().getId());
                    return ps;
                },
                keyHolder
            );

            book.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        } else {
            jdbcTemplate.update(
                UPDATE_SQL,
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPageCount(),
                book.getPerson().getId()
            );
        }

        return book;
    }

    public void delete(Book book) {
        jdbcTemplate.update(DELETE_SQL, book.getId());
    }

}
