package com.bit.muiu.controller;

import com.bit.muiu.dto.CallRoomDto;
import com.bit.muiu.dto.ChatRoomDto;
import com.bit.muiu.entity.CallRoom;
import com.bit.muiu.service.CallRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/call")
public class CallRoomController {

    private final CallRoomService callRoomService;

    // 새로운 통화 방 생성
    @PostMapping("/create")
    public ChatRoomDto createCallRoom(@RequestParam Long counselorId) {
        CallRoom callRoom = callRoomService.createCallRoom(counselorId);
        return callRoom.toDto();
    }

    // 통화 방 상태 업데이트
    @MessageMapping("/call/updateStatus")
    @SendTo("/topic/call")
    public ChatRoomDto updateStatus(CallRoomDto callRoomDto) {
        return callRoomService.updateCallRoomStatus(callRoomDto);
    }
}
