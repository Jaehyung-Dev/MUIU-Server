package com.bit.muiu.service.impl;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.entity.FundPost;
import com.bit.muiu.repository.FundPostRepository;
import com.bit.muiu.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundPostRepository fundPostRepository;
    private final String uploadPath = "C:\\lecture\\muiu-image\\"; // 파일이 저장될 경로 지정

    @Override
    public FundPostDto createFundPost(FundPostDto fundPostDto, MultipartFile file) {
        try {
            // 파일 저장
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath + fileName);
            Files.write(filePath, file.getBytes());

            // 파일명을 DB에 저장
            FundPost fundPost = fundPostDto.toEntity(); // DTO를 엔티티로 변환
            fundPost.setMainImage(fileName);  // mainImage 필드에 파일명 설정
            fundPost.setUsername(fundPostDto.getUsername()); // 사용자 이름 설정

            // DB 저장
            FundPost savedFundPost = fundPostRepository.save(fundPost);
            return savedFundPost.toDto(); // 저장된 엔티티를 다시 DTO로 변환하여 반환
        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public List<FundPostDto> getAllPosts() {
        return fundPostRepository.findAll()
                .stream()
                .map(FundPost::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FundPostDto getPostById(Long id) {
        return fundPostRepository.findById(id)
                .map(FundPost::toDto)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

}
