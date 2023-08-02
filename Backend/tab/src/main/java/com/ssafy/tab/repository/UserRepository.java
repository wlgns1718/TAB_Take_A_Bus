package com.ssafy.tab.repository;

import com.ssafy.tab.domain.User;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u.salt from User u where u.userId = :userId")
    Optional<String> findSalt(@Param("userId")String userId);
    List<User> findByUserId(String userId);
    Optional<User> findById(Long id); // 단건 Optional

    /*public UserDto loginUser(UserDto userDto) {
        
    }*/
}
