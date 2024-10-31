package com.bit.muiu.service;

import com.bit.muiu.dto.CallRoomDto;
import com.bit.muiu.entity.CallRoom;
import com.bit.muiu.entity.ChatRoom;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.CallRoomRepository;
import com.bit.muiu.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallRoomService {

    private final CallRoomRepository callRoomRepository;
    private final MemberRepository memberRepository;

    public CallRoom createCallRoom(Long memberId) {
        Member counselor = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("상담자를 찾을 수 없습니다."));

        CallRoom callRoom = CallRoom.builder()
                .counselor(counselor)
                .status("WAITING")
                .build();

        return callRoomRepository.save(callRoom);
    }

    public CallRoom enterCallRoom(Long memberId) {
        Member client = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("내담자를 찾을 수 없습니다."));

        CallRoom callRoom = callRoomRepository.findFirstByStatusOrderByIdAsc("WAITING")
                .orElseThrow(() -> new EntityNotFoundException("대기 중인 상담방이 없습니다."));

        callRoom.setClient(client);
        callRoom.setStatus("BUSY");

        return callRoomRepository.save(callRoom);
    }
}
