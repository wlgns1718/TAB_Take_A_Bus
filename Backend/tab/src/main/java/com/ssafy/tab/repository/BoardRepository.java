package com.ssafy.tab.repository;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //게시글 등록, 수정, 삭제, 게시글 클릭 시 자세히 보기, 게시글 머리말대로 가져오기, 전체 게시물 조회하기.
    //게시글 검색한대로 가져오기.
    //게시글 pageable은 pageable 인터페이스 사용할 것.
    
    //머리말로 검색하기
    Page<Board> findBySort(Sort sort, Pageable paging);

    //제목으로 검색하기
    Page<Board> findByTitleContaining(String title, Pageable paging);

    //작성자로 검색하기
    @Query("select b from Board b where b.user.userId like %:userId%")
    Page<Board> findByUserContaining(@Param("userId") String userId, Pageable paging);

    //내용으로 검색하기
    Page<Board> findByContentContaining(String cotent, Pageable paging);
}
