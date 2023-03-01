package com.example.uservalidation.service;

import com.example.uservalidation.entity.User;
import com.example.uservalidation.model.CreateUserRequest;
import com.example.uservalidation.model.UpdateUserRequest;
import com.example.uservalidation.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(String id) {
        log.info("find user by {}", id);
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean exists(String id) {
        return userRepository.findById(id).isPresent();
    }

    @Override
    public User saveUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setId(userRequest.getId());
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setAccountInstagram(userRequest.getAccountInstagram());

        log.info("saved new user");
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        log.info("invoked all data's users");
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String name) {
        val user = userRepository.findById(name).orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public Optional<User> editUser(String id, UpdateUserRequest updateUserRequest) {
        val user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setName(updateUserRequest.getName());
        user.setEmail(updateUserRequest.getEmail());
        user.setAccountInstagram(updateUserRequest.getAccountInstagram());
        userRepository.save(user);

        return Optional.of(user);
    }

    @Override
    public void deleteUsers() {
        userRepository.deleteAll();
    }
}
