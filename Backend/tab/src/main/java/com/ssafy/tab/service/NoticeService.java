package com.ssafy.tab.service;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.dto.NoticeDto;
import com.ssafy.tab.repository.NoticeRepository;
import com.ssafy.tab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public void write(Notice notice){

    }


}
