package com.ssafy.tab.dto;

import com.ssafy.tab.domain.Sort;
import lombok.Data;

@Data
public class BoardRequestDto {
    String title;
    String content;
    Sort sort;
}
