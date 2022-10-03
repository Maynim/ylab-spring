package com.edu.ulab.app.integration.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.repository.PersonRepository;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты репозитория {@link PersonRepository}.
 */
@SystemJpaTest
@RequiredArgsConstructor
@DisplayName("Интеграционное тестирование PersonRepository.")
public class PersonRepositoryTest {

    private final PersonRepository personRepository;
    private final EntityManager entityManager;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    // given
    private static final Person TEST_PERSON =
            Person.builder()
                    .id(1001L)
                    .title("reader")
                    .fullName("default user")
                    .email("email")
                    .age(55)
                    .build();

    private static final Integer COUNT_TEST_PERSON = 1;

    @DisplayName("Сохранить пользователя. Число insert - 1")
    @Test
    void insertPerson_thenAssertDmlCount() {

        // given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        // when
        Person result = personRepository.save(person);

        // then
        assertThat(result.getAge()).isEqualTo(111);
        assertSelectCount(0);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Обновить пользователя. Число select - 1, update - 1.")
    void updatePerson_thenAssertDmlCount() {

        // given
        Person updatePerson = Person.builder()
                .id(TEST_PERSON.getId())
                .fullName("Some name")
                .title("Some title")
                .email("Some email")
                .age(101)
                .build();

        // when
        Person savedPerson = personRepository.save(updatePerson);
        entityManager.flush();

        // then
        assertThat(savedPerson.getAge()).isEqualTo(101);

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Получить пользователя. Число select - 1.")
    void getPerson_thenAssertDmlCount() {

        // then
        Optional<Person> findPerson = personRepository.findById(TEST_PERSON.getId());

        // when
        assertEquals(TEST_PERSON, findPerson.orElse(null));

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Получить всех пользователей. Число select - 1.")
    void getAllPerson_thenAssertDmlCount() {

        // then
        List<Person> personList = personRepository.findAll();

        // when
        assertEquals(COUNT_TEST_PERSON, personList.size());

        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DisplayName("Удалить пользователя. Число select - 2, delete - 1.")
    void deletePerson_thenAssertDmlCount() {

        // then
        personRepository.delete(TEST_PERSON);
        entityManager.flush();

        // when
        assertTrue(personRepository.findById(TEST_PERSON.getId()).isEmpty());

        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }
}
