package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.web.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userRequestToUserDto(UserRequest userRequest);

    UserDto userEntityToUserDto(UserEntity userEntity);

    UserRequest userDtoToUserRequest(UserDto userDto);

    UserEntity userRequestToUserEntity(UserRequest userRequest);

    UserEntity userDtoToUserEntity(UserDto userDto);
}
