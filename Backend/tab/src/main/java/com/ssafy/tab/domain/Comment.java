package com.ssafy.tab.domain;

import com.ssafy.tab.dto.CommentRequestDto;
import com.ssafy.tab.dto.CommentResponseDto;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "COMMENT")
public class Comment {

    /*
    id : 댓글 번호
    userNo : 유저 번호 fk
    boardNo : 게시판 번호 fk
    content : 내용
    createTime : 작성날짜
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_NO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "USER_NO")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @Column(name = "CONTENT", length = 200)
    private String content;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    //게시글의 내용을 수정하는 기능
    public void changeComment(CommentRequestDto commentRequestDto, LocalDateTime localDateTime) {
        this.content = commentRequestDto.getContent();
        this.createTime = localDateTime;
    }

    public static Comment toEntity(CommentRequestDto commentRequestDto, User user, Board board, LocalDateTime localDateTime){
        return Comment.builder()
                .user(user)
                .board(board)
                .content(commentRequestDto.getContent())
                .createTime(localDateTime)
                .build();
    }
}
