package com.ssafy.tab.service;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.dto.NoticeResponseDto;
import com.ssafy.tab.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true) // 공지사항 전체 조회
    public Page<NoticeResponseDto> list(Pageable pageable){ // 공지사항 전체 조회(페이징)
        Page<Notice> page = noticeRepository.findAll(pageable);
        return page.map(b -> new NoticeResponseDto(b.getId(), b.getUser().getName(), b.getTitle(), b.getContent(), b.getCreateTime()));
    }

        public Long createNotice(Notice notice){ // 공지사항 생성
        Notice result = noticeRepository.save(notice);
        return result.getId();
    }
    public void deleteNotice(Notice notice){ // 게시글 삭제
        noticeRepository.delete(notice);
    } // 공지사항 삭제

    public Long modifyNotice(Notice notice){ // 공지사항 수정
        Notice result = noticeRepository.save(notice);
        return result.getId();
    }

    @Transactional(readOnly = true) // 공지사항 상세 조회
    public Notice findById(Long id){
        Optional<Notice> notice = noticeRepository.findById(id);
        if(notice.isPresent()){ // 결과가 있으면 return
            return notice.get();
        }else{ // 없으면 null
            return null;
        }
    }


}
