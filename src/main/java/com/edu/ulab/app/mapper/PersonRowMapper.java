package com.edu.ulab.app.mapper;

import com.edu.ulab.app.entity.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Person.builder()
            .id(rs.getLong("id"))
            .fullName(rs.getString("full_name"))
            .title(rs.getString("title"))
            .age(rs.getInt("age"))
            .build();
    }
}
