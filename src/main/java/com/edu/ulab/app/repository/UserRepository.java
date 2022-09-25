package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRepository extends BaseRepository<UserEntity> {

    public UserRepository() {
        super("user");
    }
}
