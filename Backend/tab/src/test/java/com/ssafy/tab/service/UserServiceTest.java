package com.ssafy.tab.service;

import com.ssafy.tab.domain.User;
import com.ssafy.tab.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    @Test
    public void 회원가입() throws Exception{
        //given
        User user = new User();
        user.setName("song");
        user.setUserId("mc.thd");
        user.setUserPw("123");

        //when
        Long id = userService.joinUser(user);

        //then
        Assert.assertEquals(user,userRepository.findOne(id));
    }
}