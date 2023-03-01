package com.example.uservalidation.controller;

import com.bazaarvoice.jolt.JsonUtils;
import com.example.uservalidation.UserValidationApplication;
import com.example.uservalidation.entity.User;
import com.example.uservalidation.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = UserValidationApplication.class
)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserRestControllerIntegrationTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private UserRepository userRepository;


    @After
    public void resetDb() {
        userRepository.deleteAll();
    }

    private List<User> users = Arrays.asList(
            new User("AM01", "Mualim", "mualim@mail.com", "@mualim"),
            new User("AM02", "Syahrido", "syahrido@mail.com", "@syahrido"),
            new User("AM03", "Adi", "adi@mail.com", "@adi")
    );

    @Test
    public void createUserTest() throws Exception {
        mock.perform(MockMvcRequestBuilders.post("/api/save-user").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJsonString(users.get(0))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<User> found = userRepository.findAll();
        Assertions.assertNotNull(found);
        Assertions.assertTrue(found.stream().anyMatch(u -> u.getName().contains("Mualim")));
        Assertions.assertFalse(found.stream().anyMatch(u -> u.getName().contains("Gita")));
    }

    @Test
    public void createListUserTest() {
        userRepository.saveAll(users);
        List<User> found = userRepository.findAll();
        Assertions.assertSame(3, found.size());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        userRepository.saveAll(users);


        mock.perform(MockMvcRequestBuilders.get("/api/get-user/{0}", users.get(0).getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        userRepository.saveAll(users);

        mock.perform(MockMvcRequestBuilders.get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void editUserTest() throws Exception {
        userRepository.saveAll(users);

        User user = new User();
        user.setName("Mualim");
        user.setEmail("");
        user.setAccountInstagram("");
        mock.perform(MockMvcRequestBuilders.put("/api/edit-user/{0}", users.get(0).getId()).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJsonString(user)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
