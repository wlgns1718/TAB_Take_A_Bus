package com.ssafy.tab.service;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.domain.Comment;
import com.ssafy.tab.domain.Sort;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.BoardRequestDto;
import com.ssafy.tab.dto.BoardResponseDto;
import com.ssafy.tab.dto.CommentResponseDto;
import com.ssafy.tab.repository.BoardRepository;
import com.ssafy.tab.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    //게시글 등록, 수정, 삭제, 게시글 클릭 시 자세히 보기, 전체 게시물 조회
    //게시글 머리말대로 가져오기, 게시글 검색어로 가져오기, 게시글 id로 게시글 조회하기.

    //전체 게시물 조회
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> list(Pageable pageable) {
        Page<Board> page = boardRepository.findAll(pageable);
        return page.map(b -> new BoardResponseDto(b.getId(), b.getUser().getUserId(), b.getTitle(), b.getContent(), b.getCreateTime(), b.getSort()));
    }

    //게시글 등록
    public void createBoard(BoardRequestDto boardRequestDto, String userId){
        User user = userService.findByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        boardRepository.save(new Board(user, boardRequestDto.getTitle(), boardRequestDto.getContent(), now, boardRequestDto.getSort()));
    }

    //게시글 삭제
    public void deleteBoard(Long boardId){
        boardRepository.delete(boardRepository.findById(boardId).get());
    }

    //게시글 수정
    public void modifyBoard(Long boardId, BoardRequestDto boardRequestDto){
        Board board = boardRepository.findById(boardId).get();
        board.changeBoard(boardRequestDto, LocalDateTime.now());
    }

    //게시글 자세히보기
    @Transactional(readOnly = true)
    public BoardResponseDto boardDetail(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        BoardResponseDto boardResponseDto = BoardResponseDto.toDto(board);
        List<Comment> comments = commentRepository.findByBoard(board);
        for (Comment comment : comments) {
            boardResponseDto.addComment(CommentResponseDto.toDto(comment));
        }
        return boardResponseDto;
    }

    //게시글 머리말대로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> boardBySort(Sort sort, Pageable pageable) {
        Page<Board> postsList = boardRepository.findBySort(sort, pageable);
        return postsList.map(b -> new BoardResponseDto(b.getId(), b.getUser().getUserId(), b.getTitle(), b.getContent(), b.getCreateTime(), b.getSort()));
    }

    //게시글 작성자 이름으로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> boardByUser(String userName, Pageable pageable) {
        Page<Board> postsList = boardRepository.findByUserContaining(userName, pageable);
        return postsList.map(b -> new BoardResponseDto(b.getId(), b.getUser().getUserId(), b.getTitle(), b.getContent(), b.getCreateTime(), b.getSort()));
    }

    //게시글 제목으로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> boardByTitle(String string, Pageable pageable) {
        Page<Board> postsList = boardRepository.findByTitleContaining(string, pageable);
        return postsList.map(b -> new BoardResponseDto(b.getId(), b.getUser().getUserId(), b.getTitle(), b.getContent(), b.getCreateTime(), b.getSort()));
    }

    //게시글 내용으로로 검색하기 + 페이징 처리
    @Transactional(readOnly = true)
    public Page<BoardResponseDto> boardByContent(String string, Pageable pageable) {
        Page<Board> postsList = boardRepository.findByContentContaining(string, pageable);
        return postsList.map(b -> new BoardResponseDto(b.getId(), b.getUser().getUserId(), b.getTitle(), b.getContent(), b.getCreateTime(), b.getSort()));
    }

    //게시글 id로 게시글 조회하기.
    @Transactional(readOnly = true)
    public Board findBoard(Long id) {
        Board board = boardRepository.findById(id).orElse(null);
        return board;
    }
}