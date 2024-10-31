package com.bit.muiu.service;

import com.bit.muiu.dto.ChatRoomDto;
import com.bit.muiu.entity.ChatRoom;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.ChatRoomRepository;
import com.bit.muiu.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository, MemberRepository memberRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }

    public ChatRoom createChatRoom(Long memberId) {
        Member counselor = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("상담사를 찾을 수 없습니다."));

        ChatRoom chatRoom = ChatRoom.builder()
                .counselor(counselor)
                .status("WAITING")
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom enterChatRoom(Long memberId) {
        Member client = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("내담자를 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findFirstByStatusOrderByIdAsc("WAITING")
                .orElseThrow(() -> new EntityNotFoundException("대기 중인 상담방이 없습니다."));

        chatRoom.setClient(client);
        chatRoom.setStatus("BUSY");

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoomDto findById(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        return chatRoom.toDto();
    }

    @Transactional
    public void updateStatusToExit(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        chatRoom.setStatus("EXIT");
        chatRoomRepository.save(chatRoom);
    }
}
