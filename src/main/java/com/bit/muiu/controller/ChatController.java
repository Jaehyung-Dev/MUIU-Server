package com.bit.muiu.controller;

import com.bit.muiu.dto.ChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Payload;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessage) {
        // 메시지 타입에 따라 다른 처리
        if ("EMOJI".equals(chatMessage.getType())) {
            // 이모지 메시지 처리
            chatMessage.setContent(chatMessage.getContent());
        } else {
            // 일반 텍스트 메시지 처리
            chatMessage.setContent(chatMessage.getContent());
        }
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDto addUser(@Payload ChatMessageDto chatMessage) {
        chatMessage.setContent(chatMessage.getSender() + "님이 입장하셨습니다.");
        return chatMessage;
    }
}
