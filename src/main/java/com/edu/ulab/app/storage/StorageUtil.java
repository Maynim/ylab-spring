package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import static com.edu.ulab.app.storage.Storage.createNewTable;
import static com.edu.ulab.app.storage.Storage.insertInto;

public class StorageUtil {

    protected static void generateStorage() {
        createNewTable("user");
        createNewTable("book");
    }

    protected static void insertIntoStorage() {
        BookEntity bookEntity = BookEntity.builder()
                .title("Abc")
                .build();

        BookEntity bookEntity2 = BookEntity.builder()
                .title("Zxc")
                .build();

        List<BookEntity> bookList = new ArrayList<>(List.of(bookEntity, bookEntity2));

        UserEntity userEntity = UserEntity.builder()
                .fullName("Alex")
                .bookList(bookList)
                .build();

        insertInto("book", bookEntity);
        insertInto("book", bookEntity2);
        insertInto("user", userEntity);
    }
}
