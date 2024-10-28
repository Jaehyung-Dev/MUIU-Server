package com.bit.muiu.controller;

import com.bit.muiu.dto.DisasterMessageDto;
import com.bit.muiu.service.DisasterMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disaster-messages")
public class DisasterMessageController {

    private final DisasterMessageService disasterMessageService;

    @Autowired
    public DisasterMessageController(DisasterMessageService disasterMessageService) {
        this.disasterMessageService = disasterMessageService;
    }

    @GetMapping("/category")
    public List<DisasterMessageDto> getMessagesByCategory(@RequestParam String category) {
        return disasterMessageService.getMessagesByCategory(category);
    }
}
