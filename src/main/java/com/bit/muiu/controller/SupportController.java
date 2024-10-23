package com.bit.muiu.controller;

import com.bit.muiu.dto.SupportRequestDto;
import com.bit.muiu.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/support")
public class SupportController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<String> sendContactEmail(@RequestBody SupportRequestDto request) {
        String to = "muiu.service@gmail.com";  // 사이트 전용 이메일 주소
        String subject = "사용자 문의";  // 이메일 제목

        // 사용자 입력을 이메일 본문에 포함
        String emailContent = String.format(
                "이름: %s\n이메일: %s\n메시지: %s",
                request.getName(),
                request.getEmail(),
                request.getMessage()
        );

        // 서비스 호출하여 이메일 발송
        emailService.sendEmail(to, subject, emailContent);

        return ResponseEntity.ok("이메일이 전송되었습니다.");
    }
}
