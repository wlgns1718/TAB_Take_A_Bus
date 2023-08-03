package com.ssafy.tab.service;

import com.ssafy.tab.domain.Notice;
import com.ssafy.tab.domain.Role;
import com.ssafy.tab.domain.User;
import com.ssafy.tab.dto.NoticeDto;
import com.ssafy.tab.repository.NoticeRepository;
import com.ssafy.tab.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@TestPropertySource(locations="classpath:/application-test.properties")
class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void 공지사항_작성_테스트() throws Exception{
        //given
        User user = new User("thd", "123", "송민철", "thdalscjf05@naver.com", Role.USER);
        Long userId = userService.joinUser(user);

        NoticeDto noticeDto = new NoticeDto("title1","content");

        Notice notice = new Notice(user,noticeDto.getTitle(),noticeDto.getContent(), LocalDateTime.now());

        //when
        Long noticeId = noticeService.createNotice(notice);
        Notice findedNotice = noticeService.findById(noticeId);
        //then
        Assertions.assertEquals(notice,findedNotice);
    }

    @Test
    public void 공지사항_조회_테스트() throws Exception{
        //given
        User user = new User("thd", "123", "송민철", "thdalscjf05@naver.com", Role.USER);
        Long userId = userService.joinUser(user);

        NoticeDto noticeDto1 = new NoticeDto("title1","content1");
        NoticeDto noticeDto2 = new NoticeDto("title2","content2");
        NoticeDto noticeDto3 = new NoticeDto("title3","content3");


        Notice notice1 = new Notice(user,noticeDto1.getTitle(),noticeDto1.getContent(), LocalDateTime.now());
        Notice notice2 = new Notice(user,noticeDto2.getTitle(),noticeDto2.getContent(), LocalDateTime.now());
        Notice notice3 = new Notice(user,noticeDto3.getTitle(),noticeDto3.getContent(), LocalDateTime.now());


        //when
        Long noticeId1 = noticeService.createNotice(notice1);
        Long noticeId2 = noticeService.createNotice(notice2);
        Long noticeId3 = noticeService.createNotice(notice3);


        //then
        List<Notice> noticeList = noticeService.getNotice();

        for (Notice notice : noticeList) {
            System.out.println(notice);
        }

    }

    @Test
    public void 공지사항_수정_테스트() throws Exception{
        //given
        User user = new User("thd", "123", "송민철", "thdalscjf05@naver.com", Role.USER);
        Long userId = userService.joinUser(user);

        NoticeDto noticeDto = new NoticeDto("title1","content1");
        Notice notice = new Notice(user,noticeDto.getTitle(),noticeDto.getContent(), LocalDateTime.now());
        Long noticeId = noticeService.createNotice(notice);

        //when
        Notice findedNotice = noticeService.findById(noticeId);
        System.out.println("수정 전 notice : " + findedNotice);

        findedNotice.changeTitle("수정한 제목!");
        findedNotice.changeContent("수정한 내용!");
        findedNotice.changeTime(LocalDateTime.now());

        Long id = noticeService.modifyNotice(findedNotice);

        Notice result = noticeService.findById(id);

        System.out.println("수정 후 notice : " + result);

        //then
        Assertions.assertEquals(findedNotice,result);
    }

    @Test
    public void 공지사항_삭제_테스트() throws Exception{
        //given
        User user = new User("thd", "123", "송민철", "thdalscjf05@naver.com", Role.USER);
        Long userId = userService.joinUser(user);

        Notice notice1 = new Notice(user,"title1","content1", LocalDateTime.now());
        Notice notice2 = new Notice(user,"title2","content2", LocalDateTime.now());
        Notice notice3 = new Notice(user,"title3","content3", LocalDateTime.now());
        Notice notice4 = new Notice(user,"title4","content4", LocalDateTime.now());

        Long noticeId = noticeService.createNotice(notice);


        //when
        List<Notice> nList = noticeService.getNotice();
        for (Notice notice1 : nList) {
            System.out.println(notice1);
        }


        //then
    }

}