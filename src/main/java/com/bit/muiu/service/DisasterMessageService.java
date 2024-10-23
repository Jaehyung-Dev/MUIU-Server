package com.bit.muiu.service;

import com.bit.muiu.dto.DisasterMessageDto;

import java.util.List;

public interface DisasterMessageService {
    List<DisasterMessageDto> getMessagesByCategory(String category);
}
