package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.PersonDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.web.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto personToPersonDto(Person person);

    Person personDtoToPerson(PersonDto personDto);

    PersonDto userRequestToPersonDto(UserRequest personRequest);
}
