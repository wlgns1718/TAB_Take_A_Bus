package com.ssafy.tab.repository;

import com.ssafy.tab.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;


    public void joinUser(User user) {
        em.persist(user);
    }

    public String getSalt(String userId) {
        User user = em.find(User.class, userId);
        return user.getSalt();
    }


    public List<User> findByUserId(@Param("userId") String userId) {
        return em.createQuery("select u from User u where u.userId = :userId",User.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    /*public UserDto loginUser(UserDto userDto) {
        
    }*/
}
