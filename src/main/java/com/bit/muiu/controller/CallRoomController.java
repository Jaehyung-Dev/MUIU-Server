package com.bit.muiu.controller;

import com.bit.muiu.dto.CallRoomDto;
import com.bit.muiu.dto.MemberDto;
import com.bit.muiu.entity.CallRoom;
import com.bit.muiu.service.CallRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/call")
@Slf4j
@CrossOrigin(origins = "https://www.마음이음.site")
public class CallRoomController {
    private final CallRoomService callRoomService;

    @PostMapping("/create")
    public CallRoomDto createCallRoom(@RequestBody Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        CallRoom callRoom = callRoomService.createCallRoom(id);
        return callRoom.toDto();
    }

    @PostMapping("/enter")
    public CallRoomDto enterCallRoom(@RequestBody Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        CallRoom callRoom = callRoomService.enterCallRoom(id);
        return callRoom.toDto();
    }

}
