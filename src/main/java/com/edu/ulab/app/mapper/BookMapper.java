package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.web.request.BookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    @Mapping(source = "person.id", target = "userId")
    BookDto bookToBookDto(Book book);

    @Mapping(source = "userId", target = "person.id")
    Book bookDtoToBook(BookDto bookDto);
}
