package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Person;
import org.springframework.stereotype.Component;

@Component
public class UserRepository extends BaseRepository<Person> {

    public UserRepository() {
        super("user");
    }
}
