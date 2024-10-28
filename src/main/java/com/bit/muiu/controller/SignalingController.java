// src/main/java/com/yourproject/controller/SignalingController.java
package com.bit.muiu.controller;

import com.bit.muiu.service.SignalingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;

@Controller
public class SignalingController {

    private final SignalingService signalingService;

    public SignalingController(SignalingService signalingService) {
        this.signalingService = signalingService;
    }

    @MessageMapping("/sendSDP")
    public void handleSDP(@Payload String sdp, String roomId) {
        signalingService.processSDP(sdp, roomId);
    }

    @MessageMapping("/sendICE")
    public void handleICE(@Payload String iceCandidate, String roomId) {
        signalingService.processICE(iceCandidate, roomId);
    }
}
