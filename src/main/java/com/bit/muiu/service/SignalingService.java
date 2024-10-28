package com.bit.muiu.service;

import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class SignalingService {

    private final SimpMessagingTemplate messagingTemplate;

    public SignalingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void processSDP(String sdp, String roomId) {
        if (sdp.contains("offer")) {
            System.out.println("Processing SDP offer...");
            messagingTemplate.convertAndSend("/topic/sdp/" + roomId, sdp);
        } else if (sdp.contains("answer")) {
            System.out.println("Processing SDP answer...");
            messagingTemplate.convertAndSend("/topic/sdp/" + roomId, sdp);
        } else {
            System.err.println("Invalid SDP type received.");
        }
    }

    public void processICE(String iceCandidate, String roomId) {
        System.out.println("Processing ICE candidate...");
        messagingTemplate.convertAndSend("/topic/ice/" + roomId, iceCandidate);
    }
}
