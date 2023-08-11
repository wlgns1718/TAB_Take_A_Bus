package com.ssafy.tab.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "USER")
public class User {

    /*
    id : primary key
    userId : 아이디
    userPw : 비밀번호
    name : 이름
    salt : 해싱을 위한 salt
    token : 토큰
    refreshToken : 리프레시 토큰
    email : 이메일
    role : 역할
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "REFRESH_TOKEN", length = 500)
    private String refreshToken;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userPw='" + userPw + '\'' +
                ", name='" + name + '\'' +
                ", salt='" + salt + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    public User(String userId, String userPw, String name, String email, Role role) {
        this.userId = userId;
        this.userPw = userPw;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public User(String userId, String name, String email, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}

