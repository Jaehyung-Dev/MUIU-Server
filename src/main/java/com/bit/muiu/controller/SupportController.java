package com.bit.muiu.controller;

import com.bit.muiu.dto.SupportRequestDto;
import com.bit.muiu.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SupportController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<String> sendContactEmail(@RequestBody SupportRequestDto request) {
        emailService.sendEmail(request.getEmail(), "문의사항", request.getMessage());
        return  ResponseEntity.ok("이메일이 전송되었습니다.");
    }
}
