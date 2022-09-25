package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.web.request.BookRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    BookDto bookEntityToBookDto(BookEntity bookEntity);

    BookRequest bookDtoToBookRequest(BookDto bookDto);

    BookEntity bookRequestToBookEntity(BookRequest bookRequest);

    BookEntity bookDtoToBookEntity(BookDto bookDto);
}
