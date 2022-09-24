package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto getUserById(Long id);

    void deleteUserById(Long id);

    void setBookList(UserDto createdUser, List<BookDto> bookDtos);

    BookDto addBookToUser(BookDto bookDto, UserDto mappedUser);

    List<BookDto> getUserBooks(Long userId);
}
