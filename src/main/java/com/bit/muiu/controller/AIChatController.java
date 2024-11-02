package com.bit.muiu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai-counseling")
public class AIChatController {
    private String pseudo_ai = "";

    @PostMapping
    public ResponseEntity<Void> sendMessage(@RequestParam String msg) {
        pseudo_ai = msg;
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> getMessage(){
        return ResponseEntity.ok(pseudo_ai);
    }
}