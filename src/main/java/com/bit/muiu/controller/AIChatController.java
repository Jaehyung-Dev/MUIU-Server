package com.bit.muiu.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai-counseling")
public class AIChatController {
    private final String CLOVA_API_URL = "https://clovastudio.apigw.ntruss.com/testapp/v1/tasks/00vkmpow/completions";
    private final String API_KEY = "JJt1bCYviC2webkzHJYo3VNLyTKIssg6q6gKZvcb";

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + API_KEY);

        // 요청 본문 설정
        Map<String, String> body = new HashMap<>();
        body.put("query", userMessage);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // 클로바 API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                CLOVA_API_URL,
                HttpMethod.POST,
                entity,
                Map.class
        );

        // 클로바 응답 처리
        Map<String, Object> responseBody = response.getBody();
        String aiResponse = (String) responseBody.get("result"); // 응답 데이터 추출

        // 프론트엔드로 응답 반환
        Map<String, String> result = new HashMap<>();
        result.put("reply", aiResponse);
        return result;
    }
}
