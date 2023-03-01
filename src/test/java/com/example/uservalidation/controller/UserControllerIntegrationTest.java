package com.example.uservalidation.controller;

import com.bazaarvoice.jolt.JsonUtils;
import com.example.uservalidation.entity.User;
import com.example.uservalidation.model.CreateUserRequest;
import com.example.uservalidation.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void whenPostUserThenCreateUser() throws Exception {
        User user = new User("AM01", "Mualim", "mualim@mail.com", "@mualim");
        given(userService.saveUser(new CreateUserRequest(user.getId(), user.getName(), user.getEmail(), user.getAccountInstagram()))).willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/save-user").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.toJsonString(new CreateUserRequest(user.getId(), user.getName(), user.getEmail(), user.getAccountInstagram()))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(Collections.EMPTY_LIST)));
        verify(userService, VerificationModeFactory.times(1)).saveUser(Mockito.any());
        reset(userService);
    }

    @Test
    public void givenAllUser() throws Exception {
        List<User> users = Arrays.asList(
                new User("AM01", "Mualim", "mualim@mail.com", "@mualim"),
                new User("AM02", "Syahrido", "syahrido@mail.com", "@syahrido"),
                new User("AM03", "Adi", "adi@mail.com", "@adi")
        );

        given(userService.getUsers()).willReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.users", hasSize(3)));

        verify(userService, VerificationModeFactory.times(1)).getUsers();
        reset(userService);
    }
}
