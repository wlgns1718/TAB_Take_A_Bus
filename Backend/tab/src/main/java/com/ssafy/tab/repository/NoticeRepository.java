package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Announcement;
import com.ssafy.tab.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository  extends JpaRepository<Announcement,Long> {
}
