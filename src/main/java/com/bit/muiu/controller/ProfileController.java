package com.bit.muiu.controller;

import com.bit.muiu.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("userId") Long userId) {
        try {
            String imageUrl = profileService.uploadProfileImage(file, userId);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }
}
