package com.bit.muiu.controller;// SignalingController.java
import com.bit.muiu.dto.SignalMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;

    public SignalingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendOffer")
    @SendTo("/topic/offer")
    public SignalMessage sendOffer(SignalMessage message) {
        return message;
    }

    @MessageMapping("/sendAnswer")
    @SendTo("/topic/answer")
    public SignalMessage sendAnswer(SignalMessage message) {
        return message;
    }

    @MessageMapping("/sendIceCandidate")
    @SendTo("/topic/candidate")
    public SignalMessage sendIceCandidate(SignalMessage message) {
        return message;
    }
}
