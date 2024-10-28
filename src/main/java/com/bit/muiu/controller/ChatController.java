package com.bit.muiu.controller;

import com.bit.muiu.dto.ChatMessageDto;
import com.bit.muiu.dto.ChatPartnerDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.ChatService;
import com.bit.muiu.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatService chatService;
    @Autowired
    private MemberService memberService;


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException e) {
        return e.getMessage();
    }

    @MessageMapping("/chat/enter")
    public void enterChatRoom(Long memberId) {
        memberService.updateMemberStatus(memberId, "WAITING");
    }

    @MessageMapping("/chat/connect")
    public void connectChat(Long memberId) {
        memberService.updateMemberStatus(memberId, "BUSY");
    }

    @MessageMapping("/chat/exit")
    public void exitChat(Long memberId) {
        memberService.updateMemberStatus(memberId, "IDLE");
    }

    @GetMapping("/partner/{userId}")
    public ResponseEntity<?> getChatPartner(@PathVariable Long userId) {
        try {
            ChatPartnerDto chatPartnerDto = chatService.findChatPartner(userId);
            ResponseDto<ChatPartnerDto> responseDto = new ResponseDto<>();
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(chatPartnerDto);
            return ResponseEntity.ok(responseDto);
        } catch (EntityNotFoundException e) {
            log.error("Error fetching chat partner by user ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("매칭 가능한 채팅 상대를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("Unexpected error fetching chat partner by user ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching chat partner");
        }
    }


    @PostMapping("/processMessage")
    public ChatMessageDto processMessage(@RequestBody ChatMessageDto chatMessage) {
        // MessageType에 따라 메시지 처리
        if (chatMessage.getType() != null) {
            switch (chatMessage.getType()) {
                case EMOJI:
                    // 이모지 메시지 처리
                    chatMessage.setContent(chatMessage.getContent());
                    break;
                case JOIN:
                    // 사용자 입장 처리
                    chatMessage.setContent(chatMessage.getSender() + "님이 입장하셨습니다.");
                    break;
                case LEAVE:
                    // 사용자 퇴장 처리
                    chatMessage.setContent(chatMessage.getSender() + "님이 퇴장하셨습니다.");
                    break;
                case CHAT:
                default:
                    // 일반 텍스트 메시지 처리
                    chatMessage.setContent(chatMessage.getContent());
                    break;
            }
        }
        return chatMessage;
    }
}
