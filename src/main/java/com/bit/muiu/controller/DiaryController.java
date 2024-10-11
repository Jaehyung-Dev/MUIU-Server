package com.bit.muiu.controller;

import com.bit.muiu.dto.DiaryDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/write")
    public ResponseEntity<?> writeDiary(@RequestBody DiaryDto diaryDto) {
        ResponseDto<DiaryDto> responseDto = new ResponseDto<>();

        // 여기에 log 추가: 요청으로 들어온 DiaryDto 확인
        log.info("Received DiaryDto: {}", diaryDto);

        try {
//            log.info("Writing diary for member ID: {}", diaryDto.getId());
            DiaryDto savedDiary = diaryService.writeDiary(diaryDto);

            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("Diary created successfully");
            responseDto.setItem(savedDiary);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error while writing diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}
