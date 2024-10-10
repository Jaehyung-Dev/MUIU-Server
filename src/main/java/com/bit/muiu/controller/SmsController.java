package com.bit.muiu.controller;

import com.bit.muiu.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/send-sms/{to}")
    public ResponseEntity<String>sendSms(@PathVariable("to") String to) {
        ResponseEntity<String> response = smsService.sendSms(to);
        return response;
    }
}
