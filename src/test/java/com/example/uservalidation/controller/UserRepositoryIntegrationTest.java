package com.example.uservalidation.controller;

import com.example.uservalidation.entity.User;
import com.example.uservalidation.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private List<User> users = Arrays.asList(
            new User("AM01", "Mualim", "mualim@mail.com", "@mualim"),
            new User("AM02", "Syahrido", "syahrido@mail.com", "@syahrido"),
            new User("AM03", "Adi", "adi@mail.com", "@adi")
    );

    @Test
    public void whenFindByIdThenReturnUser() {
        User mualim = users.get(0);
        entityManager.persist(mualim);

        User found = userRepository.findById(mualim.getId()).orElse(null);
        Assertions.assertEquals(found, mualim);
    }
}
