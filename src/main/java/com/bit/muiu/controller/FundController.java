package com.bit.muiu.controller;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.FundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/fund")
@RequiredArgsConstructor
@Slf4j
public class FundController {

    private final FundService fundService;

    private final String imageUploadPath = "C:\\lecture\\muiu-image\\"; // 이미지 저장 경로

    @PostMapping("/post")
    public ResponseEntity<?> createFundPost(
            @RequestPart("file") MultipartFile file,
            @RequestParam("post") String postString,
            @AuthenticationPrincipal UserDetails userDetails) {
        ResponseDto<FundPostDto> responseDto = new ResponseDto<>();

        try {
            // JSON 데이터를 FundPostDto 객체로 변환
            ObjectMapper mapper = new ObjectMapper();
            FundPostDto fundPostDto = mapper.readValue(postString, FundPostDto.class);

            // 인증 정보 확인 및 사용자 이름 설정
            if (userDetails == null) {
                throw new RuntimeException("사용자 인증 정보가 없습니다. 로그인 후 다시 시도해주세요.");
            }

            // 세션에서 사용자 이름 가져오기
            String loggedInUsername = userDetails.getUsername();
            log.info("loggedInUsername: {}", loggedInUsername);
            fundPostDto.setUsername(loggedInUsername);

            // DB에 저장
            FundPostDto savedFundPost = fundService.createFundPost(fundPostDto, file);

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

    @CrossOrigin(origins = "http://localhost:3000") // 프론트엔드와 동일한 포트로 접근 허용
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/image")
    public ResponseEntity<Resource> returnImage(@RequestParam String imageName) {
        try {
            Resource resource = new FileSystemResource(Paths.get(imageUploadPath + imageName));
            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
