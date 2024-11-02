package com.bit.muiu.call;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/call")
public class CallRoomController {

    private final CallRoomService callRoomService;
    private final CallRoomRepository callRoomRepository;

    @Autowired
    public CallRoomController(CallRoomService callRoomService, CallRoomRepository callRoomRepository) {
        this.callRoomService = callRoomService;
        this.callRoomRepository = callRoomRepository;
    }

    @GetMapping("/waitingRoom")
    public ResponseEntity<Optional<CallRoom>> getFirstWaitingRoom() {
        Optional<CallRoom> waitingRoom = callRoomService.findFirstWaitingRoom();
        if (waitingRoom != null) {
            return ResponseEntity.ok(waitingRoom);
        } else {
            return ResponseEntity.noContent().build(); // 대기 중인 방이 없는 경우
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CallRoom> createCallRoom(@RequestBody CallRoomDto callRoomDto) {
        CallRoom callRoom = callRoomService.createCallRoom(callRoomDto);
        CallRoom savedRoom = callRoomRepository.save(callRoom);
        return ResponseEntity.ok(savedRoom);
    }


    @PostMapping("/join")
    public ResponseEntity<String> joinCallRoom(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Optional<CallRoom> optionalCallRoom = callRoomService.findFirstWaitingRoom();

        if (optionalCallRoom.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No waiting room available.");
        }

        CallRoom callRoom = optionalCallRoom.get();

        if (!"WAITING".equals(callRoom.getRoomStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Room is not available for joining.");
        }
        callRoom.setClientId(userId);
        callRoom.setRoomStatus("BUSY");
        CallRoom updatedRoom = callRoomRepository.save(callRoom);
        return ResponseEntity.ok("Joined room successfully.");
    }

    @MessageMapping("/audio/offer")
    @SendTo("/topic/audio")
    public OfferMessage handleOffer(OfferMessage offer) {
        return offer;
    }

    @MessageMapping("/audio/answer")
    @SendTo("/topic/audio")
    public AnswerMessage handleAnswer(AnswerMessage answer) {
        return answer;
    }

    @MessageMapping("/audio/candidate")
    @SendTo("/topic/audio")
    public CandidateMessage handleCandidate(CandidateMessage candidate) {
        return candidate;
    }
}
