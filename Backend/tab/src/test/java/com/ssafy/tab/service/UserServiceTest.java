package com.ssafy.tab.service;

import com.ssafy.tab.domain.Role;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.repository.UserRepository;
import org.junit.Assert;
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
@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = false)
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
        user.setUserId("mc.the.max");
        user.setUserPw("123");
        user.setName("이수");
        user.setEmail("1234@1234");
        user.setRole(Role.USER);

        //when
        Long id = userService.joinUser(user);

        //then
        Assert.assertEquals(user,userRepository.findOne(id));
    }
}