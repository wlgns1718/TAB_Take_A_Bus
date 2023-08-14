package com.ssafy.tab.service;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @Test
    public void 회원가입() throws Exception{
        //given

        User user = new User("mc.thd","123","송","thd@naver.com", Role.USER);

        //when
        Long id = userService.joinUser(user);

        //then
        Assert.assertEquals(user,userRepository.findById(id).get());


    }

//    @Before
//    public void cleanup() {
//        userRepository.deleteAllInBatch();
//    }
//    @After
//    public void cleanAfter(){
//        userRepository.deleteAllInBatch();
//    }

}