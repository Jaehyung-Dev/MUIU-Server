package com.bit.muiu.dto;

// SignalMessage.java
public class SignalMessage {
    private String type; // offer, answer, candidate
    private String sdp; // Offer 또는 Answer의 SDP
    private String candidate; // ICE Candidate 정보
    private String roomId; // 채팅방 ID

    // Getters and Setters
}
