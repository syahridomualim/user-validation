package com.example.uservalidation.controller;

import com.example.uservalidation.entity.User;
import com.example.uservalidation.repository.UserRepository;
import com.example.uservalidation.service.UserService;
import com.example.uservalidation.service.UserServiceImp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserServiceImplIntegrationTest {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Autowired
        private UserRepository userRepository;

        @Bean
        public UserService userService() {
            return new UserServiceImp(userRepository);
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private List<User> users = Arrays.asList(
            new User("AM01", "Mualim", "mualim@mail.com", "@mualim"),
            new User("AM02", "Syahrido", "syahrido@mail.com", "@syahrido"),
            new User("AM03", "Adi", "adi@mail.com", "@adi")
    );

    @Before
    public void setup() {
        User mualim = users.get(0);
        User syahrido = users.get(1);
        User adi = users.get(2);

        userRepository.saveAll(users);

        Mockito.when(userRepository.findById(mualim.getId())).thenReturn(Optional.of(mualim));
        Mockito.when(userRepository.findById(syahrido.getId())).thenReturn(Optional.of(syahrido));
        Mockito.when(userRepository.findById(adi.getId())).thenReturn(Optional.of(adi));
        Mockito.when(userRepository.findById("wrong_id")).thenReturn(Optional.empty());
        Mockito.when(userRepository.findAll()).thenReturn(users);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void whenValidIdThenUserShouldBeFoundTest() {
        String id = "AM02";
        User user = userService.findById(id);

        Assertions.assertEquals(id, user.getId());
    }

    @Test
    public void whenValidIdThenUserShouldExist() {
        String id = "AM01";
        boolean existsUser = userService.exists(id);
        Assertions.assertTrue(existsUser);

        verifyFindByIdIsCalledOnce(id);
    }

    @Test
    public void whenInValidIdThenUserShouldBeFoundTest() {
        String id = "wrong_id";
        boolean existUser = userService.exists(id);
        Assertions.assertFalse(existUser);

        verifyFindByIdIsCalledOnce(id);
    }

    @Test
    public void givenThreeUsers() {
        List<User> allUsers = userService.getUsers();
        Assertions.assertEquals(users, allUsers);
        Assertions.assertEquals(3, allUsers.size());
        verifyFindAllEmployeeCalledOnce();
    }

    private void verifyFindByIdIsCalledOnce(String id) {
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findById(id);
        Mockito.reset(userRepository);
    }

    private void verifyFindAllEmployeeCalledOnce(){
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(userRepository);
    }
}