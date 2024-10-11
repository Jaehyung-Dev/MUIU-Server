package com.bit.muiu.controller;

import com.bit.muiu.dto.ChatMessageDto;
import com.bit.muiu.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public ChatMessageDto sendMessage(ChatMessageDto chatMessageDto) {
        chatService.saveMessage(chatMessageDto);  // 메시지를 데이터베이스에 저장
        return chatMessageDto;
    }

}
