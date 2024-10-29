package com.bit.muiu.controller;

import com.bit.muiu.dto.ChatMessageDto;
import com.bit.muiu.dto.ChatPartnerDto;
import com.bit.muiu.dto.ChatRoomDto;
import com.bit.muiu.dto.MemberDto;
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
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    private MemberService memberService;

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
//        memberService.updateMemberStatus(memberId, "WAITING");
        ChatRoom chatRoom = chatRoomService.createChatRoom(member.getId());
        return chatRoom.toDto();
    }

    @PostMapping("/enter")
    public ChatRoomDto enterChatRoom(@RequestBody MemberDto member) {
            ChatRoom chatRoom = chatRoomService.enterChatRoom(member.getId());
            return chatRoom.toDto();
//        chatService.updateStatusToBusy(memberId);
    }

    @MessageMapping("/connect")
    public void connectChat(@Payload Long memberId) {
//        memberService.updateMemberStatus(memberId, "BUSY");
    }

    @MessageMapping("/exit")
    public void exitChat(@Payload Long memberId) {
        chatService.updateStatusToIdle(memberId);
    }
}
