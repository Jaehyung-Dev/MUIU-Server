package com.bit.muiu.controller;

import com.bit.muiu.dto.*;
import com.bit.muiu.entity.ChatRoom;
import com.bit.muiu.entity.Member;
import com.bit.muiu.service.ChatRoomService;
import com.bit.muiu.service.ChatService;
import com.bit.muiu.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "https://www.마음이음.site")
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException e) {
        return e.getMessage();
    }

    @MessageMapping("/chat.sendMessage/{chatRoomId}")
    @SendTo("/topic/{chatRoomId}/messages")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessage) {
        return chatMessage;
    }

    @PostMapping("/create")
    public ChatRoomDto createChatRoom(@RequestBody MemberDto member) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(member.getId());
        return chatRoom.toDto();
    }

    @PostMapping("/enter")
    public ChatRoomDto enterChatRoom(@RequestBody MemberDto member) {
            ChatRoom chatRoom = chatRoomService.enterChatRoom(member.getId());
            ChatRoomDto chatRoomDto = chatRoom.toDto();
            chatRoomDto.setEnter(true);
            return chatRoomDto;
    }

    @MessageMapping("/chat.connect/{chatRoomId}")
    @SendTo("/topic/{chatRoomId}/messages")
    public ChatMessageDto connectChat(@DestinationVariable Long chatRoomId) {
        log.info("connectChat: " + chatRoomId);
        ChatMessageDto messageDto = new ChatMessageDto();
        ChatRoomDto chatRoomDto = chatRoomService.findById(chatRoomId);

        messageDto.setClientName(chatRoomDto.getClientName());
        messageDto.setCounselorName(chatRoomDto.getCounselorName());
        messageDto.setContent(chatRoomDto.getClientName() + "님이 입장하셨습니다.");
        messageDto.setType(ChatMessageDto.MessageType.JOIN);
        return messageDto;
    }

    @PostMapping("/exit/{chatRoomId}")
    public ResponseEntity<Void> exitChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.updateStatusToExit(chatRoomId);

        ChatMessageDto leaveMessage = new ChatMessageDto();
        leaveMessage.setContent("상대가 나갔습니다.");
        leaveMessage.setType(ChatMessageDto.MessageType.LEAVE);

        messagingTemplate.convertAndSend("/topic/" + chatRoomId + "/messages", leaveMessage);

        return ResponseEntity.ok().build();
    }
}
