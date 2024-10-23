package com.bit.muiu.service.impl;

import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.MemberRepository;
import com.bit.muiu.service.ProfileService;
import com.bit.muiu.service.NaverCloudStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final NaverCloudStorageService storageService;
    private final MemberRepository memberRepository;

    public ProfileServiceImpl(NaverCloudStorageService storageService, MemberRepository memberRepository) {
        this.storageService = storageService;
        this.memberRepository = memberRepository;
    }

    @Override
    public String uploadProfileImage(MultipartFile file, Long userId) throws IOException {
        String fileName = "profile-images/" + userId + "/" + file.getOriginalFilename();
        String imageUrl = storageService.uploadFile(file, fileName);

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        member.setProfileImageUrl(imageUrl);
        memberRepository.save(member);

        return imageUrl;
    }
}
