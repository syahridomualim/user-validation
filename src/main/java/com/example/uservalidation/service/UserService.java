package com.example.uservalidation.service;

import com.example.uservalidation.entity.User;
import com.example.uservalidation.model.CreateUserRequest;
import com.example.uservalidation.model.UpdateUserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findById(String id);

    boolean exists(String id);

    User saveUser(CreateUserRequest userRequest);

    List<User> getUsers();

    void deleteUser(String name);

    Optional<User> editUser(String id, UpdateUserRequest updateUserRequest);

    void deleteUsers();
}
