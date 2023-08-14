package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Role;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinDto {
    private String id;
    private String pw;
    private String name;
    private String email;
    private Role role;

    public UserJoinDto(String id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
