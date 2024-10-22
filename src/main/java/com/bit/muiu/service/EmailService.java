package com.bit.muiu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);  // 수신자는 고정된 사이트 이메일 주소(muiu.service@gmail.com)
        message.setSubject(subject);  // 제목은 "사용자 문의"
        message.setText(text);  // 본문에는 사용자가 남긴 정보가 포함
        emailSender.send(message);
    }
}
