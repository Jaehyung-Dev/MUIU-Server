package com.bit.muiu.service;

import org.springframework.http.ResponseEntity;

public interface SmsService {

    ResponseEntity<String> sendSms(String to);
}
