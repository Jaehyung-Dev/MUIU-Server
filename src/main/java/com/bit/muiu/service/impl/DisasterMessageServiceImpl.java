package com.bit.muiu.service.impl;

import com.bit.muiu.dto.DisasterMessageDto;
import com.bit.muiu.entity.DisasterMessage;
import com.bit.muiu.repository.DisasterMessageRepository;
import com.bit.muiu.service.DisasterMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisasterMessageServiceImpl implements DisasterMessageService {

    private final DisasterMessageRepository disasterMessageRepository;

    @Autowired
    public DisasterMessageServiceImpl(DisasterMessageRepository disasterMessageRepository) {
        this.disasterMessageRepository = disasterMessageRepository;
    }

    @Override
    public List<DisasterMessageDto> getMessagesByCategory(String category) {
        List<DisasterMessage> messages = disasterMessageRepository.findByCategory(category);
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DisasterMessageDto convertToDto(DisasterMessage disasterMessage) {
        return new DisasterMessageDto(
                disasterMessage.getId(),
                disasterMessage.getCategory(),
                disasterMessage.getAlertLevel(),
                disasterMessage.getEventContent(),
                disasterMessage.getOccurrenceTime(),
                disasterMessage.getReadStatus(),
                disasterMessage.getMessageContent()
        );
    }
}
