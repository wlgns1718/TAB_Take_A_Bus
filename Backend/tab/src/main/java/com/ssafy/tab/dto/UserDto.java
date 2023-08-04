package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@Getter @Setter
public class UserDto {
    private String id;
    private String pw;
    private String name;
    private String email;
    private Role role;
}
