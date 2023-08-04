package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations="classpath:/application-test.properties")
@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:/application-test.properties")
class UserRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Test
    public void salt() throws Exception{
        User user = new User("thdalscjf","123","송민철","thdalscjf05@naver.com", Role.USER);
        userService.joinUser(user);

        System.out.println(user.getUserId());
        System.out.println("salt = " + userRepository.findSalt(user.getUserId()).get());
    }
}