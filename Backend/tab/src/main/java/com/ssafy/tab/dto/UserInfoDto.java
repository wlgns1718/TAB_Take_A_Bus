package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Role;

import javax.persistence.*;

public class UserInfoDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long id;

    @Column(name = "USER_ID", length = 20)
    private String userId;

    @Column(name = "USER_PASSWORD", length = 500)
    private String userPw;

    @Column(name = "USER_NAME", length = 45)
    private String name;

    private String refreshToken;

    private String email;

    private Role role;

}
