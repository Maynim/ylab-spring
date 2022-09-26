package com.edu.ulab.app.mapper;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Book.builder()
            .id(rs.getLong("id"))
            .title(rs.getString("title"))
            .author(rs.getString("author"))
            .pageCount(rs.getInt("page_count"))
            .person(
                Person.builder()
                .id(rs.getLong("person_id"))
                .build()
            )
            .build();
    }
}
