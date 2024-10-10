package com.bit.muiu.service.impl;

import com.bit.muiu.provider.SmsProvider;
import com.bit.muiu.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsProvider smsProvider;

    @Override
    public ResponseEntity<String> sendSms(String to) {
        try {
            String verificationCode = smsProvider.sendSms(to); // 인증번호를 받아옵니다.
            return ResponseEntity.status(HttpStatus.OK).body("메세지 전송에 성공했습니다. 인증번호: " + verificationCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("메세지 전송 중 에러가 발생했습니다.");
        }
    }
}
