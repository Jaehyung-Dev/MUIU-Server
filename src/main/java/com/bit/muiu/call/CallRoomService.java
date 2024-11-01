package com.bit.muiu.call;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CallRoomService {

    private final CallRoomRepository callRoomRepository;

    @Autowired
    public CallRoomService(CallRoomRepository callRoomRepository) {
        this.callRoomRepository = callRoomRepository;
    }

    @Transactional
    public CallRoom createCallRoom(CallRoomDto callRoomDto) {
        CallRoom callRoom = new CallRoom();
        callRoom.setCounselorId(callRoomDto.getCounselorId());
        callRoom.setRoomStatus("WAITING");
        return callRoomRepository.save(callRoom);
    }

    @Transactional
    public CallRoom joinRoom(Long userId) {
        CallRoom callRoom = findFirstWaitingRoom()
                .orElseThrow(() -> new RuntimeException("No waiting room available"));

        callRoom.setClientId(userId);
        callRoom.setRoomStatus("BUSY");

        return callRoomRepository.save(callRoom);
    }

    public Optional<CallRoom> findFirstWaitingRoom() {
        return callRoomRepository.findFirstByRoomStatus("WAITING");
    }

    public List<CallRoom> findRoomByUserId(Long userId) {
        return callRoomRepository.findByCounselorIdOrClientId(userId, userId);
    }


}
