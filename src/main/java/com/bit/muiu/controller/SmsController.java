package com.bit.muiu.controller;

import com.bit.muiu.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/send/{to}")
    public ResponseEntity<Map<String,String>> sendSms(@PathVariable("to") String to) {
        
        String verificationCode = smsService.sendSms(to); // 인증번호 받기

        Map<String, String> response = new HashMap<>(); // 응답 데이터를 Map에 추가
        response.put("verificationCode", verificationCode);

        return ResponseEntity.ok(response);
    }
}
