package com.ssafy.tab.service;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.dto.NoticeDto;
import com.ssafy.tab.dto.NoticeListResponseDto;
import com.ssafy.tab.dto.NoticeResponseDto;
import com.ssafy.tab.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true) // 공지사항 전체 조회
    public Page<NoticeListResponseDto> list(Pageable pageable){ // 공지사항 전체 조회(페이징)
        Page<Notice> page = noticeRepository.findAll(pageable);
        return page.map(b -> new NoticeListResponseDto(b.getId(), b.getUser().getName(), b.getTitle(),  b.getCreateTime()));
    }

    @Transactional(readOnly = true) // 공지사항 상세 조회
    public NoticeResponseDto findById(Long id){
        Optional<Notice> notice = noticeRepository.findById(id);
        System.out.println(notice.get());
        if(notice.isPresent()){ // 결과가 있으면 return
            Notice n = notice.get();
            return new NoticeResponseDto(n.getId(),n.getUser().getName(),n.getTitle(),n.getContent(),n.getCreateTime());

        }else{ // 없으면 null
            return null;
        }
    }

    public Long createNotice(Notice notice){ // 공지사항 생성
        Notice result = noticeRepository.save(notice);
        return result.getId();
    }

    // 공지사항 삭제
    public boolean deleteNotice(Long userNo, Long noticeNo){ // 게시글 삭제

        Notice notice = noticeRepository.findById(noticeNo).get();
        if(notice.getUser().getId() == userNo){
            noticeRepository.delete(notice);
            return true;
        }
        return false;

    }

    public Long modifyNotice(Long userNo, Long noticeNo, NoticeDto noticeDto){ // 공지사항 수정
        Notice notice = noticeRepository.findById(noticeNo).get();
        if(notice.getUser().getId() == userNo){
            notice.changeTitle(noticeDto.getTitle());
            notice.changeContent(noticeDto.getContext());
            notice.changeTime(Timestamp.valueOf(LocalDateTime.now()));
            return notice.getId();
        }
        return -1l;
    }




}
