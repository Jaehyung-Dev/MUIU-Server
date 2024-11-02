package com.bit.muiu.controller;

import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/ai-counseling/copy")
public class AIChatControllerCopy {

    @Value("${clova.host}")
    private String host;

    @Value("${clova.api_key}")
    private String apiKey;

    @Value("${clova.api_key_primary_val}")
    private String apiKeyPrimaryVal;

    @Value("${clova.request_id}")
    private String requestId;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public String chat(@RequestBody Map<String, String> request) {
        String text = request.get("text");

        // 기본값을 포함한 요청 데이터 구성
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("text", text);
        requestData.put("start", "");
        requestData.put("restart", "");
        requestData.put("includeTokens", false);
        requestData.put("topP", 0.8);
        requestData.put("topK", 4);
        requestData.put("maxTokens", 300);
        requestData.put("temperature", 0.85);
        requestData.put("repeatPenalty", 5.0);
        requestData.put("stopBefore", new String[]{"<|endoftext|>"});
        requestData.put("includeAiFilters", true);
        requestData.put("includeProbs", false);

        String url = "https://" + host + "/testapp/v1/tasks/haygjo9s/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        headers.set("X-NCP-APIGW-API-KEY", apiKeyPrimaryVal);
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && "20000".equals(((Map) responseBody.get("status")).get("code"))) {
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