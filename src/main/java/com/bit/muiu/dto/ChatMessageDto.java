package com.bit.muiu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatMessageDto {
    private String sender;
    private String content;
    private MessageType type;
    private String counselorName;
    private String clientName;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        EMOJI
    }
}
