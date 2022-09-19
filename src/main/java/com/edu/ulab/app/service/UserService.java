package com.edu.ulab.app.service;

import com.edu.ulab.app.entity.UserEntity;

public interface UserService {
    UserEntity createUser(UserEntity userEntity);

    UserEntity updateUser(UserEntity userEntity);

    UserEntity getUserById(Long id);

    void deleteUserById(Long id);
}
