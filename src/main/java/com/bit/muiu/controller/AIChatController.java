package com.bit.muiu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

    private final CompletionExecutor completionExecutor;

    public AIChatController(
            @Value("${clova.host}") String host,
            @Value("${clova.api_key}") String apiKey,
            @Value("${clova.api_key_primary_val}") String apiKeyPrimaryVal,
            @Value("${clova.request_id}") String requestId) {
        this.completionExecutor = new CompletionExecutor(host, apiKey, apiKeyPrimaryVal, requestId);
    }

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        // 요청 데이터 설정
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("text", userMessage);
        requestData.put("start", "");
        requestData.put("restart", "");
        requestData.put("includeTokens", true);
        requestData.put("topP", 0.8);
        requestData.put("topK", 0);
        requestData.put("maxTokens", 100);
        requestData.put("temperature", 0.5);
        requestData.put("repeatPenalty", 5.0);
        requestData.put("stopBefore", new String[]{});
        requestData.put("includeAiFilters", true);

        // 클로바 API 호출
        String aiResponse = completionExecutor.execute(requestData);

        // 프론트엔드로 응답 반환
        Map<String, String> result = new HashMap<>();
        result.put("reply", aiResponse);
        return result;
    }

    // CompletionExecutor 내부 클래스
    private static class CompletionExecutor {
        private final String host;
        private final String apiKey;
        private final String apiKeyPrimaryVal;
        private final String requestId;

        public CompletionExecutor(String host, String apiKey, String apiKeyPrimaryVal, String requestId) {
            this.host = host;
            this.apiKey = apiKey;
            this.apiKeyPrimaryVal = apiKeyPrimaryVal;
            this.requestId = requestId;
        }

        public String execute(Map<String, Object> completionRequest) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json; charset=utf-8");
                headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
                headers.set("X-NCP-APIGW-API-KEY", apiKeyPrimaryVal);
                headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(completionRequest, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> response = restTemplate.postForEntity("https://" + host + "/testapp/v1/tasks/00vkmpow/completions", entity, Map.class);

                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.get("status").equals("20000")) {
                    return (String) ((Map) responseBody.get("result")).get("text");
                } else {
                    return "Error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }
    }
}
