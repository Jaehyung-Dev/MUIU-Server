package com.bit.muiu.controller;

import com.bit.muiu.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counsel")
public class CounselController {

    private final CounselService counselService;

    @PostMapping("/migrate")
    public String migrateData() {
        counselService.migrateExistingData();
        return "Existing data migration to Counsel completed successfully!";
    }
}
