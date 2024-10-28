package com.bit.muiu.controller;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.FundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // 프론트엔드 도메인 허용
@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
@Slf4j
public class FundController {

    private final FundService fundService;

    @PostMapping("/post")
    public ResponseEntity<?> createFundPost(
            @RequestBody FundPostDto fundPostDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDto<FundPostDto> responseDto = new ResponseDto<>();

        try {
            // 인증 정보 확인 및 사용자 이름 설정
            if (userDetails == null) {
                throw new RuntimeException("사용자 인증 정보가 없습니다. 로그인 후 다시 시도해주세요.");
            }

            // 세션에서 사용자 이름 가져오기
            String loggedInUsername = userDetails.getUsername();
            log.info("loggedInUsername: {}", loggedInUsername);
            fundPostDto.setUsername(loggedInUsername);

            // DB에 저장
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

    @GetMapping("/posts")
    public ResponseEntity<List<FundPostDto>> getAllPosts() {
        try {
            List<FundPostDto> posts = fundService.getAllPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            log.error("Error retrieving fund posts: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<FundPostDto> getPostById(@PathVariable Long id) {
        try {
            FundPostDto post = fundService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            log.error("Error retrieving post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<?> updateFundPost(
            @PathVariable Long postId,
            @RequestBody FundPostDto fundPostDto) {

        ResponseDto<FundPostDto> responseDto = new ResponseDto<>();

        try {
            fundService.updateFundPost(postId, fundPostDto);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Fund post updated successfully");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error updating fund post: ", e);

            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Error updating fund post: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


}
