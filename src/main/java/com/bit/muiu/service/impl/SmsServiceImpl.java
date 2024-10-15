package com.bit.muiu.service.impl;

import com.bit.muiu.provider.SmsProvider;
import com.bit.muiu.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsProvider smsProvider;

    @Override
    public String sendSms(String to) {
        // 인증번호를 받아와 그대로 반환
        return smsProvider.sendSms(to);
    }
}