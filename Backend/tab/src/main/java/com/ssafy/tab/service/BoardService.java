package com.ssafy.tab.service;

import com.ssafy.tab.domain.Board;
import com.ssafy.tab.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    BoardRepository boardRepository;

    //게시글 등록, 수정, 삭제 , 가져오기, 게시글 머리말대로 가져오기,

    //게시글 등록
    public Board createBoard(Board board){
        Board saveBoard = boardRepository.save(board);
        return saveBoard;
    }

    //게시글 삭제
    public void deleteBoard(Board board){
        boardRepository.delete(board);
    }


}
