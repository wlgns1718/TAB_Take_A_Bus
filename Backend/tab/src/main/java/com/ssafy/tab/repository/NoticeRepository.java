package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository  extends JpaRepository<Notice,Long> {
}
