package com.ssafy.tab.repository;

import com.ssafy.tab.domain.User;
import com.ssafy.tab.service.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;


    public void joinUser(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getId());
        user.setUserPw(userDto.getPw());
        user.setRole(userDto.getRole());
        user.setName(userDto.getName());
        user.setSalt(userDto.getSalt());

        em.persist(user);
    }

    public String getSalt(String userId) {
        User user = em.find(User.class, userId);
        return user.getSalt();
    }

    public UserDto loginUser(UserDto userDto) {
        
    }
}
