package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.PersonDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.PersonMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.PersonService;
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

    private final PersonService personService;
    private final BookService bookService;
    private final BookMapper bookMapper;
    private final PersonMapper personMapper;

    public UserDataFacade(PersonService personService,
                          BookService bookService,
                          BookMapper bookMapper,
                          PersonMapper personMapper) {
        this.personService = personService;
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.personMapper = personMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);

        PersonDto mappedPerson = personMapper.userRequestToPersonDto(userBookRequest.getUserRequest());
        log.info("Mapped person from request: {}", mappedPerson);

        PersonDto createdPerson = personService.createPerson(mappedPerson);
        log.info("Created person: {}", createdPerson);

        List<BookDto> bookDtoList = userBookRequest.getBookRequests()
            .stream()
            .filter(Objects::nonNull)
            .map(bookMapper::bookRequestToBookDto)
            .peek(mappedBook -> mappedBook.setUserId(createdPerson.getId()))
            .peek(mappedBook -> log.info("Mapped book: {}", mappedBook))
            .map(bookService::createBook)
            .peek(createdBook -> log.info("Created book: {}", createdBook))
            .toList();
        log.info("Collected books: {}", bookDtoList);

        List<Long> booksIdList = bookDtoList.stream()
            .map(BookDto::getId)
            .toList();

        return UserBookResponse.builder()
            .userId(createdPerson.getId())
            .booksIdList(booksIdList)
            .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Got user book update request: {}", userBookRequest);

        PersonDto currPerson = personService.getPersonById(userId);
        log.info("Person for update: {}", currPerson);

        PersonDto mappedPerson = personMapper.userRequestToPersonDto(userBookRequest.getUserRequest());
        mappedPerson.setId(userId);
        log.info("Mapped user request: {}", mappedPerson);

        PersonDto updatedUser = personService.updatePerson(mappedPerson);

        List<BookDto> bookDtoList = userBookRequest.getBookRequests()
            .stream()
            .filter(Objects::nonNull)
            .map(bookMapper::bookRequestToBookDto)
            .peek(mappedBook -> mappedBook.setUserId(updatedUser.getId()))
            .peek(mappedBook -> log.info("Mapped book: {}", mappedBook))
            .map(bookService::createBook)
            .peek(addedBook -> log.info("Added book: {}", addedBook))
            .toList();
        log.info("Collected books: {}", bookDtoList);

        log.info("Updated person: {}", updatedUser);

        List<Long> booksIdList = bookDtoList.stream()
            .map(BookDto::getId)
            .toList();

        return UserBookResponse.builder()
            .userId(updatedUser.getId())
            .booksIdList(booksIdList)
            .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        PersonDto gottenPerson = personService.getPersonById(userId);
        log.info("Gotten person: {}", gottenPerson);

        List<BookDto> bookDtoList = bookService.getBooksByPersonId(userId);

        List<Long> booksIdList = bookDtoList.stream().map(BookDto::getId).toList();

        return UserBookResponse.builder()
            .userId(gottenPerson.getId())
            .booksIdList(booksIdList)
            .build();
    }

    public ResponseEntity<BaseWebResponse> deleteUserWithBooks(Long userId) {
        List<BookDto> personBooks = bookService.getBooksByPersonId(userId);
        personBooks.forEach(book -> {
            bookService.deleteBookById(book.getId());
            log.info("Deleted book: {}", book);
        });

        PersonDto deletedUser = personService.getPersonById(userId);
        personService.deletePersonById(userId);
        log.info("Deleted person: {}", deletedUser);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new BaseWebResponse("Entities have been removed"));
    }
}
