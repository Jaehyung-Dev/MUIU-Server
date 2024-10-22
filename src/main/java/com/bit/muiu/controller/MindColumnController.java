package com.bit.muiu.controller;

import com.bit.muiu.dto.MindColumnDto;
import com.bit.muiu.service.MindColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/mind-column")
@RequiredArgsConstructor
@Slf4j
public class MindColumnController {
    private final MindColumnService mindColumnService;

    @PostMapping
    public ResponseEntity<?> post(@RequestPart("mindColumnDto") MindColumnDto mindColumnDto,
                                  @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,

                                  )
}
