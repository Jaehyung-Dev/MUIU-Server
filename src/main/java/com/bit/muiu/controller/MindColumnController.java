package com.bit.muiu.controller;

import com.bit.muiu.dto.MindColumnDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.MindColumnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mind-column")
@RequiredArgsConstructor
@Slf4j
public class MindColumnController {
    private final MindColumnService mindColumnService;

    @PostMapping
    public ResponseEntity<?> post(@RequestPart("mindColumnDto") String mindColumnDtoJson,
                                  @RequestPart(value = "uploadFiles") MultipartFile[] uploadFiles,
                                  @PageableDefault(page = 0, size = 12, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        ResponseDto<MindColumnDto> responseDto = new ResponseDto<>();

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            MindColumnDto mindColumnDto = objectMapper.readValue(mindColumnDtoJson, MindColumnDto.class);

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
    public ResponseEntity<?> getList(
            @RequestParam(defaultValue = "0") int page,
            @PageableDefault(size = 3, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        ResponseDto<MindColumnDto> responseDto = new ResponseDto<>();

        try {
            // 페이지가 0이면 초기 로드, 아니면 추가 로드로 구분
            int pageSize = (page == 0) ? 12 : pageable.getPageSize();
            Pageable updatedPageable = PageRequest.of(page, pageSize, pageable.getSort());

            Page<MindColumnDto> mindColumnList = mindColumnService.findAll(updatedPageable);

            responseDto.setPageItems(mindColumnList);
            responseDto.setItem(MindColumnDto.builder().build());
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> modify(@RequestPart("mindColumnDto") String mindColumnDtoJson,
                                    @RequestPart(value = "mcfList", required = false) List<MultipartFile> mcfList,
                                    @RequestPart(value = "deletedFiles", required = false) List<Long> deletedFileIds,
                                    @PageableDefault(page = 0, size = 12, sort = "regdate", direction = Sort.Direction.DESC) Pageable pageable) {
        ResponseDto<MindColumnDto> responseDto = new ResponseDto<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MindColumnDto mindColumnDto = objectMapper.readValue(mindColumnDtoJson, MindColumnDto.class);

            Page<MindColumnDto> modifiedMindColumnDto = mindColumnService.modify(mindColumnDto, mcfList, deletedFileIds, pageable);

            responseDto.setPageItems(modifiedMindColumnDto);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable("id") long id){
        ResponseDto<MindColumnDto> responseDto = new ResponseDto<>();

        try{
            mindColumnService.deleteById(id);

            responseDto.setStatusCode(HttpStatus.NO_CONTENT.value());
            responseDto.setStatusMessage("no content");

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
}
