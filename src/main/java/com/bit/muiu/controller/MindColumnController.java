package com.bit.muiu.controller;

import com.bit.muiu.dto.MindColumnDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.MindColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/mind-column")
@RequiredArgsConstructor
@Slf4j
public class MindColumnController {
    private final MindColumnService mindColumnService;

    @PostMapping
    public ResponseEntity<?> post(@RequestPart("mindColumnDto") MindColumnDto mindColumnDto,
                                  @RequestPart(value = "uploadFiles", required = false) MultipartFile[] uploadFiles,
                                  @PageableDefault(page = 0, size = 3) Pageable pageable
                                  ) {
        ResponseDto<MindColumnDto> responseDto = new ResponseDto<>();

        try{
            Page<MindColumnDto> mindColumnDtoList = mindColumnService.post(mindColumnDto, uploadFiles, pageable);
            responseDto.setPageItems(mindColumnDtoList);
            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");

            return ResponseEntity.created(new URI("/mindColumn")).body(responseDto);
        } catch(Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping
    public ResponseEntity<?> getList(@PageableDefault(page = 0, size = 3) Pageable pageable){
        ResponseDto<MindColumnDto> responseDto = new ResponseDto<>();

        try {
            Page<MindColumnDto> mindColumnList = mindColumnService.findAll(pageable);

            responseDto.setPageItems(mindColumnList);
            responseDto.setItem(MindColumnDto.builder().build());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");

            return ResponseEntity.ok(responseDto);
        } catch(Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}
