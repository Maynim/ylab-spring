package com.edu.ulab.app.facade;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;
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

import java.util.ArrayList;
import java.util.Collections;
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

        UserEntity mappedUser = userMapper.userRequestToUserEntity(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", mappedUser);

        UserEntity createdUser = userService.createUser(mappedUser);
        log.info("Created user: {}", createdUser);

        List<BookEntity> bookEntities = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookEntity)
                .peek(mappedBook -> mappedBook.setUserId(createdUser.getId()))
                .peek(mappedBook -> log.info("Mapped book: {}", mappedBook))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .toList();
        log.info("Collected books: {}", bookEntities);

        createdUser.setBookList(new ArrayList<>(bookEntities));

        List<Long> booksIdList = bookEntities.stream()
                .map(BookEntity::getId)
                .toList();

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Got user book update request: {}", userBookRequest);

        UserEntity mappedUser = userMapper.userRequestToUserEntity(userBookRequest.getUserRequest());
        mappedUser.setId(userId);
        log.info("Mapped user request: {}", mappedUser);

        UserEntity oldUser = userService.getUserById(userId);
        mappedUser.setBookList(oldUser.getBookList());
        UserEntity updatedUser = userService.updateUser(mappedUser);

        List<BookEntity> bookEntities = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookEntity)
                .peek(mappedBook -> mappedBook.setUserId(updatedUser.getId()))
                .peek(mappedBook -> log.info("Mapped book: {}", mappedBook))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Updated book: {}", createdBook))
                .toList();
        log.info("Collected books: {}", bookEntities);

        bookEntities.forEach(book -> updatedUser.getBookList().add(book));
        log.info("Updated user: {}", updatedUser);

        List<Long> booksIdList = updatedUser.getBookList().stream()
                .map(BookEntity::getId)
                .toList();

        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        UserEntity getUser = userService.getUserById(userId);

        List<Long> booksIdList;

        if (getUser.getBookList().isEmpty()) {
            booksIdList = Collections.emptyList();
        } else {
            booksIdList = getUser.getBookList().stream().map(BookEntity::getId).toList();
        }

        return UserBookResponse.builder()
                .userId(getUser.getId())
                .booksIdList(booksIdList)
                .build();
    }

    public ResponseEntity<BaseWebResponse> deleteUserWithBooks(Long userId) {
        UserEntity deletedUser = userService.getUserById(userId);

        deletedUser.getBookList()
                .forEach(book -> {
                    bookService.deleteBookById(book.getId());
                    log.info("Deleted book: {}", book);
                });

        userService.deleteUserById(userId);
        log.info("Deleted user: {}", deletedUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseWebResponse("Entities have been removed"));
    }
}
