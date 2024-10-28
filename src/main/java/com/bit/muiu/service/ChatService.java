package com.bit.muiu.service;

import com.bit.muiu.dto.ChatPartnerDto;
import com.bit.muiu.entity.ChatRoom;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.ChatRoomRepository;
import com.bit.muiu.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member findAvailablePartner(Long userId) {
        // 현재 사용자 제외하고 waiting 상태인 사용자 목록 가져오기
        List<Member> waitingMembers = memberRepository.findByStatus("waiting");

        // 현재 사용자를 목록에서 제외
        waitingMembers.removeIf(member -> member.getId().equals(userId));

        // 매칭 가능한 상대가 없다면 null 반환
        if (waitingMembers.isEmpty()) {
            return null;
        }

        // waiting 상태인 사용자 목록을 랜덤으로 섞고 첫 번째 사용자 선택
        Collections.shuffle(waitingMembers);
        return waitingMembers.get(0);
    }

    @Transactional
    public ChatPartnerDto findChatPartner(Long userId) {
        // 현재 사용자 확인
        Member currentUser = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 현재 사용자가 waiting 상태가 아닌 경우 예외 처리
        if (!"waiting".equalsIgnoreCase(currentUser.getStatus())) {
            throw new IllegalStateException("현재 사용자는 매칭 가능한 상태가 아닙니다.");
        }

        // waiting 상태인 사용자 목록 조회
        List<Member> waitingMembers = memberRepository.findByStatus("waiting");

        // 현재 사용자를 제외한 나머지 사용자 중 랜덤으로 상대 선택
        waitingMembers.remove(currentUser);
        Collections.shuffle(waitingMembers);

        // 상대방이 없는 경우 예외 처리
        if (waitingMembers.isEmpty()) {
            throw new EntityNotFoundException("매칭 가능한 채팅 상대를 찾을 수 없습니다.");
        }

        // 매칭 상대 설정
        Member partner = waitingMembers.get(0);
        currentUser.setStatus("busy");
        partner.setStatus("busy");

        // 상태 업데이트
        memberRepository.save(currentUser);
        memberRepository.save(partner);

        return new ChatPartnerDto(partner.getName(), partner.getRole());
    }

}
