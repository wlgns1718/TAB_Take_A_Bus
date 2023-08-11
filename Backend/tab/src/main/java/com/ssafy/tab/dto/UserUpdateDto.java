package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
public class UserUpdateDto {
    private String userPw;

    private String name;

    private String email;

}
