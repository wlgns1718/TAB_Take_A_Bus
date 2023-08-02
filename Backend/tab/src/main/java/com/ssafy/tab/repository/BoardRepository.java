package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //게시글 등록, 수정, 삭제 , 가져오기, 게시글 머리말대로 가져오기,


}
