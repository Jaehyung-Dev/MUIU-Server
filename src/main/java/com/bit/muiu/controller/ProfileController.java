package com.bit.muiu.controller;

import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.MemberRepository;
import com.bit.muiu.service.CustomUserDetails;
import com.bit.muiu.service.NaverCloudStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/apis/profile")
public class ProfileController {

    private final MemberRepository memberRepository;
    private final NaverCloudStorageService naverCloudStorageService;

    public ProfileController(MemberRepository memberRepository, NaverCloudStorageService naverCloudStorageService) {
        this.memberRepository = memberRepository;
        this.naverCloudStorageService = naverCloudStorageService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfileImage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        if (customUserDetails == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }

        Member member = memberRepository.findById(customUserDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        String profileImageUrl = member.getProfileImageUrl();
        return ResponseEntity.ok().body(Map.of("profileImageUrl", profileImageUrl));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        try {
            String fileName = "profile-images/" + customUserDetails.getId() + "/" + file.getOriginalFilename();
            String imageUrl = naverCloudStorageService.uploadFile(file, fileName);

            Member member = memberRepository.findById(customUserDetails.getId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            member.setProfileImageUrl(imageUrl);
            memberRepository.save(member);

            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

}
