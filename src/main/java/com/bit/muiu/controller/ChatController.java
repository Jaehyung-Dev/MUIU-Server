package com.bit.muiu.controller;

import com.bit.muiu.dto.ChatMessageDto;
import com.bit.muiu.dto.ChatPartnerDto;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    private MemberService memberService;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException e) {
        return e.getMessage();
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat/enter")
    public void enterChatRoom(@Payload Long memberId) {
        memberService.updateMemberStatus(memberId, "WAITING");
    }

    @MessageMapping("/chat/connect")
    public void connectChat(@Payload Long memberId) {
        memberService.updateMemberStatus(memberId, "BUSY");
    }

    @MessageMapping("/chat/exit")
    public void exitChat(@Payload Long memberId) {
        chatService.updateStatusToIdle(memberId);
    }

    @GetMapping("/partner/{userId}")
    public ResponseEntity<ChatPartnerDto> getChatPartner(@PathVariable Long userId) {
        try {
            ChatPartnerDto chatPartnerDto = chatService.findChatPartner(userId);
            chatService.updateStatusToWaiting(chatPartnerDto.getId()); // 상태 변경 필요 시 호출
            return ResponseEntity.ok(chatPartnerDto);
        } catch (Exception e) {
            log.error("매칭 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 실패 시 404 반환
        }
    }

}
