package com.ssafy.tab.service;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.BoardDto;
import com.ssafy.tab.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    //게시글 id로 게시글 엔티티 불러오기, 게시글 등록, 수정, 삭제, 게시글 클릭 시 자세히 보기, 전체 게시물 조회
    //게시글 머리말대로 가져오기, 게시글 검색어로 가져오기,

    //게시글 id로 게시글 엔티티 불러오기
    public Board findBoard(Long id){
        Board board = null;
        try{
            board = boardRepository.findById(id).get();
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return board;
    }

    //게시글 등록
    public Board createBoard(Board board){
        Board saveBoard = null;
        try{
            saveBoard = boardRepository.save(board);
        }catch (Exception e){
            System.out.println("오류 발생!");
        }finally {
            return saveBoard;
        }
    }

    //게시글 삭제
    public void deleteBoard(Board board){
        boardRepository.delete(board);
    }

    //게시글 수정
    public Board modifyBoard(BoardDto boardDto){
        Board originBoard = boardRepository.findById(boardDto.getId()).get();
        originBoard.changeBoard(boardDto);
        return originBoard;
    }

    //게시글 자세히보기
    @Transactional(readOnly = true)
    public Board selectBoardDetail(Long id) {
        Optional<Board> selectBoard = boardRepository.findById(id);
        Board board = selectBoard.get();
        return board;
    }

    //게시글 머리말대로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<Board> selectBoardSort(Sort sort, Pageable pageable) {
        Page<Board> postsList = boardRepository.findBySort(sort, pageable);
        return postsList;
    }

    //게시글 작성자로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<Board> selectBoardUser(String userId, Pageable pageable) {
        Page<Board> postsList = boardRepository.findByUserContaining(userId, pageable);
        return postsList;
    }

    //게시글 제목으로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<Board> selectBoardTitle(String string, Pageable pageable) {
        Page<Board> postsList = boardRepository.findByTitleContaining(string, pageable);
        return postsList;
    }

    //게시글 내용으로로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<Board> selectBoardContent(String string, Pageable pageable) {
        Page<Board> postsList = boardRepository.findByContentContaining(string, pageable);
        return postsList;
    }

    //전체 게시물 조회
    @Transactional(readOnly = true)
    public Page<BoardDto> selectBoard(Pageable paging) {
        Page<Board> page = boardRepository.findAll(paging);
        return page.map(b -> new BoardDto(b.getId(), b.getUser().getName(), b.getTitle(), b.getContent(), b.getCreateTime(), b.getSort()));
    }
}
