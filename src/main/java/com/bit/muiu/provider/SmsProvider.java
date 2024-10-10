package com.bit.muiu.provider;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SmsProvider {
    private final DefaultMessageService messageService;

    @Value("${sms.from-number}") String FROM;

    public SmsProvider(
            @Value("${sms.api-key}")String API_KEY,
            @Value("${sms.api-secret-key}")String API_SECRET_KEY,
            @Value("${sms.domain}")String DOMAIN
    ){
        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET_KEY, DOMAIN);
    }

    public String sendSms(String to) {
        Message message = new Message();
        message.setFrom(FROM);
        message.setTo(to);
        String numStr = generateRandomNumber();
        message.setText("마음이음 인증번호: [" + numStr + "]");

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        String statusCode = response.getStatusCode();

        if (statusCode.equals("2000")) {
            return numStr;  // 성공 시 인증번호 반환
        } else {
            throw new RuntimeException("SMS 전송에 실패했습니다.");
        }
    }

    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}
