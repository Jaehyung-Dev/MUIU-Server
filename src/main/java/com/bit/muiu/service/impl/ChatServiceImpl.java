package com.bit.muiu.service.impl;

import com.bit.muiu.dto.ChatMessageDto;
import com.bit.muiu.entity.Chat;
import com.bit.muiu.repository.ChatRepository;
import com.bit.muiu.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public void saveMessage(ChatMessageDto chatMessageDto) {
        try {
            Chat chat = Chat.builder()
                    .channelId(chatMessageDto.getChannelId())
                    .sender(chatMessageDto.getSender())
                    .message(chatMessageDto.getContent())
                    .build();
            chatRepository.save(chat);
        } catch (Exception e) {
            throw new RuntimeException("메시지 저장 중 오류 발생", e);
        }
    }
    @Override
    public List<Chat> getMessagesByChannelId(String channelId) {
        return chatRepository.findByChannelId(channelId);
    }
}
