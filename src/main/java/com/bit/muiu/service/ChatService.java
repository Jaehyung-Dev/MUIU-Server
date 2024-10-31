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
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatService(MemberRepository memberRepository) { // 생성자 주입
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void updateStatusToBusy(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        memberRepository.save(member);
    }

    @Transactional
    public void updateStatusToWaiting(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        memberRepository.save(member);
    }



}
