package com.bit.muiu.controller;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.FundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
@Slf4j
public class FundController {

    private final FundService fundService;

    @PostMapping("/post")
    public ResponseEntity<?> createFundPost(@RequestBody FundPostDto fundPostDto, @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDto<FundPostDto> responseDto = new ResponseDto<>();

        log.info("Received FundPostDto: {}", fundPostDto);

        try {
//            // 인증 정보 확인
//            if (userDetails == null) {
//                throw new RuntimeException("사용자 인증 정보가 없습니다. 로그인 후 다시 시도해주세요.");
//            }
//
//            // 세션에서 사용자 이름 가져오기
//            String loggedInUsername = userDetails.getUsername();
//            log.info("loggedInUsername: {}", loggedInUsername);
//            fundPostDto.setUsername(loggedInUsername);

            // DB에 게시글 저장
            FundPostDto savedFundPost = fundService.createFundPost(fundPostDto);

            // 성공 응답
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
}
