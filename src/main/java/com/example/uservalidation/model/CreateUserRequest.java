package com.example.uservalidation.model;

import com.example.uservalidation.validation.AccountInstagramValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserRequest {
    @NotNull(message = "Id can't be null")
    private String id;

    @Size(min = 3, max = 20, message = "Name must between 3 and 20")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @AccountInstagramValidation(message = "Account Instagram field can be null or must start by @")
    private String accountInstagram;
}
