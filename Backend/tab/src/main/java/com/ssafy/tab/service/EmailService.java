package com.ssafy.tab.service;


import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ssafy.tab.dto.EmailDto;

@Service
public class EmailService  {

    private JavaMailSender jms;

    public EmailService(JavaMailSender jms) {
        super();
        this.jms = jms;
    }

    public String makeCode(int size) {
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;
        do {
            num = ran.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }
        } while (sb.length() < size);
        return sb.toString();
    }

    public String makeHtml(String type, String code) {
        String html = null;
        switch (type) {
            case "register":
                html = "<h1>Tab 이메일 인증 코드 :"+code+"입니다.</h1>";
                break;
            case "tempPw":
                html = "<h1>Tab 임시 비밀번호는 "+code+"입니다.</h1>";
                break;
        }
        return html;
    }

    public String sendMail(EmailDto emailDto) {
        //타입에 따라
        //1. 인증코드 만들기
        //2. html string만들기
        String type = emailDto.getType();
        String email = emailDto.getEmail();
        String code = null, html = null, subject = null;
        switch(type) {
            case "register":
                code = makeCode(6);
                html = makeHtml(type, code);
                subject = "Tab - 이메일인증코드 입니다.";
                break;
            case "tempPw":
                code = makeCode(10);
                html = makeHtml(type, code);
                subject = "Tab - 임시 비밀번호 입니다.";
                break;
        }

        //공통 - 메일보내기
        MimeMessage mail = jms.createMimeMessage();
        try {
            mail.setSubject(subject,"utf-8");
            mail.setText(html,"utf-8","html");
            mail.addRecipient(RecipientType.TO, new InternetAddress(email));
            jms.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
            return "error";
        }

        return code;
    }

}