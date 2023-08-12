package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Role;
import lombok.Data;


@Data
public class UserJoinDto {
    private String id;
    private String pw;
    private String name;
    private String email;
    private Role role;
}
