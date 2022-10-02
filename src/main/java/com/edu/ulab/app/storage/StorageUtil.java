package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;

import java.util.ArrayList;
import java.util.List;

import static com.edu.ulab.app.storage.Storage.createNewTable;
import static com.edu.ulab.app.storage.Storage.insertInto;

@Deprecated
public class StorageUtil {

    protected static void generateStorage() {
        createNewTable("user");
        createNewTable("book");
    }

    protected static void insertIntoStorage() {
        Book book = Book.builder()
                .title("Abc")
                .build();

        Book book2 = Book.builder()
                .title("Zxc")
                .build();

        List<Book> bookList = new ArrayList<>(List.of(book, book2));

        Person person = Person.builder()
                .fullName("Alex")
                .bookList(bookList)
                .build();

        insertInto("book", book);
        insertInto("book", book2);
        insertInto("user", person);
    }
}
