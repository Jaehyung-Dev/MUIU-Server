package com.bit.muiu.service;

import com.bit.muiu.dto.ChatMessageDto;
import java.util.List;
import com.bit.muiu.entity.Chat;

public interface ChatService {
    void saveMessage(ChatMessageDto chatMessageDto);
    List<Chat> getMessagesByChannelId(String channelId); // 추가된 메서드
}
