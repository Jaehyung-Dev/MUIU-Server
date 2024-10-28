package com.bit.muiu.service.impl;

import com.bit.muiu.common.FileUtils;
import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.entity.FundPost;
import com.bit.muiu.repository.FundPostRepository;
import com.bit.muiu.service.FundService;
import com.bit.muiu.service.NaverCloudStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {
    private final FundPostRepository fundPostRepository;
    private final FileUtils fileUtils;
    private static final Logger log = LoggerFactory.getLogger(FundServiceImpl.class);

    @Autowired
    private NaverCloudStorageService naverCloudStorageService;

    @Override
    public FundPostDto createFundPost(FundPostDto fundPostDto) {
        try {
            // 로그 추가
            log.info("DTO 데이터 확인: {}", fundPostDto);

            // DTO를 엔티티로 변환
            FundPost fundPost = fundPostDto.toEntity();
            fundPost.setUsername(fundPostDto.getUsername()); // 사용자 이름 설정

            // mainImage 필드를 엔티티에 설정
            if (fundPostDto.getMainImage() != null) {
                fundPost.setMainImage(fundPostDto.getMainImage());
            }

            // DB에 저장
            FundPost savedFundPost = fundPostRepository.save(fundPost);
            return savedFundPost.toDto(); // 저장된 엔티티를 다시 DTO로 변환하여 반환
        } catch (Exception e) {
            throw new RuntimeException("Failed to create fund post", e);
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

    @Override
    public void updateFundPost(Long postId, FundPostDto fundPostDto) {
        Optional<FundPost> optionalPost = fundPostRepository.findById(postId);

        if (optionalPost.isPresent()) {
            FundPost post = optionalPost.get();

            // 기존 데이터를 새로운 데이터로 업데이트
            post.setTitle(fundPostDto.getTitle());
            post.setDescription(fundPostDto.getDescription());
            post.setTeamName(fundPostDto.getTeamName());
            post.setFundStartDate(fundPostDto.getFundStartDate());
            post.setFundEndDate(fundPostDto.getFundEndDate());
            post.setTargetAmount(fundPostDto.getTargetAmount());

            if (fundPostDto.getMainImage() != null) {
                post.setMainImage(fundPostDto.getMainImage());
            }

            // 저장
            fundPostRepository.save(post);
        } else {
            throw new RuntimeException("Post not found with id " + postId);
        }
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 고유한 파일 이름 생성
            String uniqueFileName = "fund-images/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 네이버 클라우드에 이미지 업로드하고 URL 반환
            return naverCloudStorageService.uploadFile(file, uniqueFileName);
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }
}
