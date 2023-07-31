package com.ssafy.tab.service;

import com.ssafy.tab.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
    private String id;
    private String pw;
    private String name;
    private String salt;
    private Role role;
}
