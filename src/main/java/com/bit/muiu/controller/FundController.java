package com.bit.muiu.controller;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.FundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
@Slf4j
public class FundController {

    private final FundService fundService;

    @PostMapping("/post")
    public ResponseEntity<?> createFundPost(@RequestBody FundPostDto fundPostDto) {
        ResponseDto<FundPostDto> responseDto = new ResponseDto<>();

        log.info("Received FundPostDto: {}", fundPostDto);

        try {
            FundPostDto savedFundPost = fundService.createFundPost(fundPostDto);

            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("Fund post created successfully");
            responseDto.setItem(savedFundPost);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (Exception e) {
            log.error("Error while creating fund post: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Failed to create fund post");
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    // 추후 추가 가능: 기부 게시글 삭제
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteFundPost(@PathVariable Long id) {
//        try {
//            fundService.deleteFundPost(id);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        } catch (Exception e) {
//            log.error("Error while deleting fund post: {}", e.getMessage());
//            return ResponseEntity.internalServerError().body("Failed to delete fund post");
//        }
//    }

    // 추후 추가 가능: 기부 게시글 수정
//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateFundPost(@PathVariable Long id, @RequestBody FundPostDto fundPostDto) {
//        try {
//            FundPostDto updatedFundPost = fundService.updateFundPost(id, fundPostDto);
//            return ResponseEntity.ok(updatedFundPost);
//        } catch (Exception e) {
//            log.error("Error while updating fund post: {}", e.getMessage());
//            return ResponseEntity.internalServerError().body("Failed to update fund post");
//        }
//    }
}
