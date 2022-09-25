package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BookMapper bookMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        log.info("Mapped user: {}", userEntity);

        UserEntity savedUser = userRepository.save(userEntity);
        log.info("Saved user: {}", savedUser);

        return userMapper.userEntityToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = userMapper.userDtoToUserEntity(userDto);
        log.info("Mapped user: {}", userEntity);

        boolean update = userRepository.update(userEntity);
        if (!update) {
            throw new NotFoundException("User not found");
        }

        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<UserEntity> user = userRepository.findById(id);

        return user.map(userMapper::userEntityToUserDto)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void setBookList(UserDto createdUser, List<BookDto> bookDtoList) {
        Optional<UserEntity> maybeUser = userRepository.findById(createdUser.getId());
        List<BookEntity> bookEntityList = bookDtoList.stream()
                .map(bookMapper::bookDtoToBookEntity)
                .toList();

        maybeUser.ifPresentOrElse(
                user -> user.setBookList(bookEntityList),
                () -> {
                    throw new NotFoundException("User not found");
                }
        );
    }

    @Override
    public BookDto addBookToUser(BookDto bookDto, UserDto mappedUser) {
        Optional<UserEntity> maybeUser = userRepository.findById(mappedUser.getId());
        BookEntity bookEntity = bookMapper.bookDtoToBookEntity(bookDto);

        maybeUser.ifPresentOrElse(
                user -> user.getBookList().add(bookEntity),
                () -> {
                    throw new NotFoundException("User not found");
                }
        );

        return bookDto;
    }

    @Override
    public List<BookDto> getUserBooks(Long userId) {
        Optional<UserEntity> maybeUser = userRepository.findById(userId);

        // из за несовершенности предложенной мной реализации хранилища
        // реализовать что-то то более аккуратное мне не удалось
        return maybeUser.map(UserEntity::getBookList)
                .map(bookEntityList -> bookEntityList.stream()
                        .map(bookMapper::bookEntityToBookDto)
                        .toList()
                )
                .orElseThrow(() -> {
                    throw new NotFoundException("User not found");
                });
    }

}
