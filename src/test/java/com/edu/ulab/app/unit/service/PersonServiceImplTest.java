package com.edu.ulab.app.unit.service;

import com.edu.ulab.app.config.UnitTest;
import com.edu.ulab.app.dto.PersonDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.PersonMapper;
import com.edu.ulab.app.repository.PersonRepository;
import com.edu.ulab.app.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.PersonServiceImpl}.
 */
@UnitTest
@DisplayName("Тестирование функциональности PersonService.")
public class PersonServiceImplTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    PersonMapper personMapper;

    @InjectMocks
    PersonServiceImpl personService;

    private static final PersonDto INPUT_PERSON =
            PersonDto.builder()
                    .fullName("Test name")
                    .title("Test title")
                    .email("Test email")
                    .age(100)
                    .build();

    private static final Person PERSON =
            Person.builder()
                    .fullName("Test name")
                    .title("Test title")
                    .email("Test email")
                    .age(100)
                    .build();

    private static final Person SAVED_PERSON =
            Person.builder()
                    .id(1L)
                    .fullName("Test name")
                    .title("Test title")
                    .email("Test email")
                    .age(100)
                    .build();

    private static final PersonDto OUTPUT_PERSON =
            PersonDto.builder()
                    .id(1L)
                    .fullName("Test name")
                    .title("Test title")
                    .email("Test email")
                    .age(100)
                    .build();

    private static final Long ID = 1L;


    @Test
    @DisplayName("Создать пользователя. Должно пройти успешно.")
    void savePerson_Test() {

        //when
        when(personMapper.personDtoToPerson(INPUT_PERSON)).thenReturn(PERSON);
        when(personRepository.save(PERSON)).thenReturn(SAVED_PERSON);
        when(personMapper.personToPersonDto(SAVED_PERSON)).thenReturn(OUTPUT_PERSON);

        //then
        PersonDto createdPerson = personService.createPerson(INPUT_PERSON);
        assertEquals(ID, createdPerson.getId());
    }

    @Test
    @DisplayName("Обновить пользователя. Должно пройти успешно.")
    void updatePerson_Test() {

        // when
        when(personMapper.personDtoToPerson(INPUT_PERSON)).thenReturn(PERSON);
        when(personRepository.save(PERSON)).thenReturn(SAVED_PERSON);
        when(personMapper.personToPersonDto(SAVED_PERSON)).thenReturn(OUTPUT_PERSON);

        // then
        PersonDto createdPerson = personService.updatePerson(INPUT_PERSON);
        assertEquals(ID, createdPerson.getId());
    }

    @Test
    @DisplayName("Получить пользователя. Должно пройти успешно.")
    void getPerson_Test() {

        //when
        when(personRepository.findById(ID)).thenReturn(Optional.of(PERSON));
        when(personMapper.personToPersonDto(PERSON)).thenReturn(OUTPUT_PERSON);

        //then
        PersonDto getPerson = personService.getPersonById(ID);
        assertEquals(ID, getPerson.getId());
    }

    @Test
    @DisplayName("Удалить пользователя. Должно пройти успешно.")
    void deletePerson_Test() {

        //when
        when(personRepository.findById(ID)).thenReturn(Optional.of(PERSON));
        doNothing().when(personRepository).delete(PERSON);

        //then
        assertDoesNotThrow(() -> personService.deletePersonById(ID));
    }
}
