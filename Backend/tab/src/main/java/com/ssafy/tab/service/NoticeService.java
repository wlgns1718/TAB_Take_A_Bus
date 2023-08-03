package com.ssafy.tab.service;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.dto.NoticeDto;
import com.ssafy.tab.repository.NoticeRepository;
import com.ssafy.tab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getNotice(){
        return noticeRepository.findAll();
    }
    public Long createNotice(Notice notice){ // 게시글 생성
        Notice result = noticeRepository.save(notice);
        return result.getId();
    }
    public void deleteNotice(Notice notice){ // 게시글 삭제
        noticeRepository.delete(notice);
    }

    public Long modifyNotice(Notice notice){ // 게시글 수정
        Notice result = noticeRepository.save(notice);
        return result.getId();
    }

    @Transactional(readOnly = true)
    public Notice findById(Long id){
        Optional<Notice> notice = noticeRepository.findById(id);
        if(notice.isPresent()){
            return notice.get();
        }else{
            return null;
        }
    }


}
