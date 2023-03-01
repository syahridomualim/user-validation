package com.example.uservalidation.controller;

import com.example.uservalidation.entity.User;
import com.example.uservalidation.model.CreateUserRequest;
import com.example.uservalidation.model.Response;
import com.example.uservalidation.model.UpdateUserRequest;
import com.example.uservalidation.service.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping({"/api"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save-user")
    public ResponseEntity<?> saveUser(@Valid @RequestBody CreateUserRequest userRequest) {
        userService.saveUser(userRequest);
        return getResponse(Collections.EMPTY_LIST);
    }

    @GetMapping("/get-user/{id}")
    public ResponseEntity<?> findUserByName(@PathVariable String id) {
        Map<String, User> dataUser = new HashMap<>();
        val user = userService.findById(id);
        dataUser.put(user.getName(), user);

        return getResponse(dataUser);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        Map<String, List<User>> usersData = new HashMap<>();
        val users = userService.getUsers();
        usersData.put("users", users);

        return getResponse(usersData);
    }

    @PutMapping("/edit-user/{id}")
    public ResponseEntity<?> editUser(@PathVariable String id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        val user = userService.editUser(id, updateUserRequest);

        return getResponse(user);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam String name) {
        userService.deleteUser(name);
        return getResponse(Collections.EMPTY_LIST);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteUsers() {
        userService.deleteUsers();
        return getResponse(Collections.EMPTY_LIST);
    }

    private static <T> ResponseEntity getResponse(T data) {
        val response = new Response(
                new Date(), HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(), data
        );

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
