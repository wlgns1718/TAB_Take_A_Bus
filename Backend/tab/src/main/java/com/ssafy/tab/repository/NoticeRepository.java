package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.dto.NoticeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository  extends JpaRepository<Notice,Long> {


    @Query("select new com.ssafy.tab.dto.NoticeResponseDto(n.user.name,n.title,n.content,n.createTime) from Notice n")
    Page<NoticeResponseDto> list(Pageable pageable);  // 공지사항 전체 페이징 결과 (dto에 담아서 리턴)

}
