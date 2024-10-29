package com.bit.muiu.service;

import com.bit.muiu.dto.CallRoomDto;
import com.bit.muiu.dto.ChatRoomDto;
import com.bit.muiu.entity.CallRoom;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.CallRoomRepository;
import com.bit.muiu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CallRoomService {

    private final CallRoomRepository callRoomRepository;
    private final MemberRepository memberRepository;

    // 새로운 통화 방 생성
    @Transactional
    public CallRoom createCallRoom(Long counselorId) {
        Member counselor = memberRepository.findById(counselorId)
                .orElseThrow(() -> new IllegalArgumentException("상담자를 찾을 수 없습니다."));

        CallRoom callRoom = CallRoom.builder()
                .counselor(counselor)
                .status("WAITING")
                .build();

        return callRoomRepository.save(callRoom);
    }

    // 통화 방 상태 업데이트
    @Transactional
    public ChatRoomDto updateCallRoomStatus(CallRoomDto callRoomDto) {
        CallRoom callRoom = callRoomRepository.findById(callRoomDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("통화 방을 찾을 수 없습니다."));

        callRoom.setStatus(callRoomDto.getStatus());
        callRoomRepository.save(callRoom);

        return callRoom.toDto();
    }
}
