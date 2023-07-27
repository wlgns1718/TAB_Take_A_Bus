package com.ssafy.tab.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class User {

    @Id @GeneratedValue
    @Column(name = "USER_NO")
    private Long id;

    @Column(name = "USER_ID", length = 20)
    private String userId;

    @Column(name = "USER_PASSWORD", length = 500)
    private String userPw;

    @Column(name = "USER_NAME", length = 45)
    private String name;

    @Column(name = "SALT", length = 32)
    private String salt;

    @Column(name = "TOKEN", length = 500)
    private String token;

    @Column(name = "REFRESH_TOKEN", length = 500)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;



}
