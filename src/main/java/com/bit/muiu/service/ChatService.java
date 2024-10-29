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
    private MemberRepository memberRepository;

    @Autowired
    public ChatService(MemberRepository memberRepository) { // 생성자 주입
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ChatPartnerDto findChatPartner(Long userId) {
        Member currentUser = memberRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<Member> matchingCandidates = memberRepository.findByStatusAndDifferentRole(
                "WAITING", currentUser.getRole()
        );

        if (matchingCandidates.isEmpty()) {
            throw new EntityNotFoundException("매칭 가능한 채팅 상대를 찾을 수 없습니다.");
        }

        Member partner = matchingCandidates.get(0);

        // 두 사용자 모두 BUSY로 설정
        currentUser.setStatus("BUSY");
        partner.setStatus("BUSY");

        memberRepository.save(currentUser);
        memberRepository.save(partner);

        return new ChatPartnerDto(partner.getName(), partner.getRole(), partner.getId());
    }



    @Transactional
    public void updateStatusToBusy(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        member.setStatus("BUSY");
        memberRepository.save(member);
    }

    @Transactional
    public void updateStatusToWaiting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        member.setStatus("WAITING");
        memberRepository.save(member);
    }

    @Transactional
    public void updateStatusToIdle(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        member.setStatus("IDLE");
        memberRepository.save(member);
    }
}
