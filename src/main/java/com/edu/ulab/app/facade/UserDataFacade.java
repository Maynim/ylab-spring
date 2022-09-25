package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.BaseWebResponse;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {

    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);

        UserDto mappedUser = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", mappedUser);

        UserDto createdUser = userService.createUser(mappedUser);
        log.info("Created user: {}", createdUser);

        List<BookDto> bookDtoList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(mappedBook -> mappedBook.setUserId(createdUser.getId()))
                .peek(mappedBook -> log.info("Mapped book: {}", mappedBook))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .toList();
        log.info("Collected books: {}", bookDtoList);

        userService.setBookList(createdUser, bookDtoList);

        List<Long> booksIdList = bookDtoList.stream()
                .map(BookDto::getId)
                .toList();

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Got user book update request: {}", userBookRequest);

        UserDto mappedUser = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        mappedUser.setId(userId);
        log.info("Mapped user request: {}", mappedUser);

        UserDto updatedUser = userService.updateUser(mappedUser);

        List<BookDto> bookDtoList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(mappedBook -> mappedBook.setUserId(updatedUser.getId()))
                .peek(mappedBook -> log.info("Mapped book: {}", mappedBook))
                .map(bookService::createBook)
                .map(addedBook -> userService.addBookToUser(addedBook, updatedUser))
                .peek(addedBook -> log.info("Added book: {}", addedBook))
                .toList();
        log.info("Collected books: {}", bookDtoList);

        log.info("Updated user: {}", updatedUser);

        List<Long> booksIdList = bookDtoList.stream()
                .map(BookDto::getId)
                .toList();

        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        UserDto getUser = userService.getUserById(userId);
        log.info("Gotten user: {}", getUser);

        List<BookDto> bookDtoList = userService.getUserBooks(userId);

        List<Long> booksIdList = bookDtoList.stream().map(BookDto::getId).toList();

        return UserBookResponse.builder()
                .userId(getUser.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public ResponseEntity<BaseWebResponse> deleteUserWithBooks(Long userId) {
        List<BookDto> userBooks = userService.getUserBooks(userId);
        userBooks.forEach(book -> {
            bookService.deleteBookById(book.getId());
            log.info("Deleted book: {}", book);
        });

        UserDto deletedUser = userService.getUserById(userId);
        userService.deleteUserById(userId);
        log.info("Deleted user: {}", deletedUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseWebResponse("Entities have been removed"));
    }
}
