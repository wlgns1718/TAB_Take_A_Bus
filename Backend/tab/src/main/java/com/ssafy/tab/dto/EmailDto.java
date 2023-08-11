package com.ssafy.tab.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class EmailDto {
    private String userId;
    private String type; // register 혹은 findPw
    private String email;
}


