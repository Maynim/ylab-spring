package com.edu.ulab.app.repository.template;

import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.PersonRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PersonTemplateRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String FIND_BY_ID_SQL = "SELECT * FROM person p WHERE p.id = ?";
    private final String UPDATE_SQL = "UPDATE person SET full_name = ?, title = ?, age = ? WHERE id = ?";
    private final String SAVE_SQL = "INSERT INTO person(full_name, title, age) VALUES (?,?,?)";
    private final String DELETE_SQL = "DELETE FROM person WHERE id = ?";

    public PersonTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Person> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, new PersonRowMapper(), id).stream().findAny();
    }

    public Person save(Person person) {
        if (person.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(SAVE_SQL, new String[]{"id"});
                    ps.setString(1, person.getFullName());
                    ps.setString(2, person.getTitle());
                    ps.setLong(3, person.getAge());
                    return ps;
                },
                keyHolder
            );

            person.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        } else {
            jdbcTemplate.update(UPDATE_SQL, person.getFullName(), person.getTitle(), person.getAge(), person.getId());
        }

        return person;
    }

    public void delete(Person person) {
        jdbcTemplate.update(DELETE_SQL, person.getId());
    }

}
