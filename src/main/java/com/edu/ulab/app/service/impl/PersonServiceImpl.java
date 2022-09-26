package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.PersonDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.PersonMapper;
import com.edu.ulab.app.repository.template.PersonTemplateRepository;
import com.edu.ulab.app.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonTemplateRepository personRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonTemplateRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }


    @Override
    public PersonDto createPerson(PersonDto personDto) {
        Person person = personMapper.personDtoToPerson(personDto);
        log.info("Mapped person: {}", person);

        Person savedPerson = personRepository.save(person);
        log.info("Saved person: {}", savedPerson);

        return personMapper.personToPersonDto(savedPerson);
    }

    @Override
    public PersonDto updatePerson(PersonDto personDto) {
        Person person = personMapper.personDtoToPerson(personDto);
        log.info("Mapped person: {}", person);

        Person savedPerson = personRepository.save(person);
        log.info("Saved person: {}", savedPerson);

        return personMapper.personToPersonDto(savedPerson);
    }

    @Override
    public PersonDto getPersonById(Long id) {
        Optional<Person> maybePerson = personRepository.findById(id);
        log.info("Received person: {}", maybePerson);

        return maybePerson.map(personMapper::personToPersonDto)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public void deletePersonById(Long id) {
        Optional<Person> maybePerson = personRepository.findById(id);
        log.info("Person for delete: {}", maybePerson);

        maybePerson.ifPresentOrElse(
            personRepository::delete,
            () -> {
                throw new NotFoundException("User not found");
            }
        );
    }
}
