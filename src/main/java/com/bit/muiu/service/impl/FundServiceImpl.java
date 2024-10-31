package com.bit.muiu.service.impl;

import com.bit.muiu.common.FileUtils;
import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.dto.FundRecordDto;
import com.bit.muiu.entity.FundPost;
import com.bit.muiu.entity.FundRecord;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.FundPostRepository;
import com.bit.muiu.repository.FundRecordRepository;
import com.bit.muiu.repository.MemberRepository;
import com.bit.muiu.service.FundService;
import com.bit.muiu.service.NaverCloudStorageService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {
    private final FundPostRepository fundPostRepository;
    private final FundRecordRepository fundRecordRepository;
    private final MemberRepository memberRepository;
    private final FileUtils fileUtils;
    private static final Logger log = LoggerFactory.getLogger(FundServiceImpl.class);

    @Autowired
    private NaverCloudStorageService naverCloudStorageService;

    @Override
    public FundPostDto createFundPost(FundPostDto fundPostDto) {
        try {
            log.info("DTO 데이터 확인: {}", fundPostDto);

            FundPost fundPost = fundPostDto.toEntity();
            fundPost.setUsername(fundPostDto.getUsername());

            if (fundPostDto.getMainImage() != null) {
                fundPost.setMainImage(fundPostDto.getMainImage());
            }

            FundPost savedFundPost = fundPostRepository.save(fundPost);
            return savedFundPost.toDto();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create fund post", e);
        }
    }


    @Override
    public List<FundPostDto> getAllPosts() {
        return fundPostRepository.findAll().stream()
                .map(fundPost -> {
                    FundPostDto dto = new FundPostDto(fundPost);
                    if (dto.getMainImage() != null && !dto.getMainImage().isEmpty()) {
                        // mainImage에 이미 URL이 포함되어 있는지 확인하여 중복 방지
                        if (!dto.getMainImage().startsWith("http")) {
                            String imageUrl = fileUtils.getFileUrl("fund-images/", dto.getMainImage());
                            dto.setMainImage(imageUrl);
                        }
                    }
                    return dto;
                })
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
            String fileName = "fund-images/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // NaverCloudStorageService를 이용해 이미지 업로드 및 URL 반환
            return naverCloudStorageService.uploadFile(file, fileName);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }




    @Override
    public void deleteFundPost(Long postId) {
        // 예외 처리를 포함하여 게시글 삭제 로직 수행
        if (!fundPostRepository.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        fundPostRepository.deleteById(postId); // 게시글 삭제
    }

    @Override
    @Transactional
    public void savePaymentRecord(FundRecordDto fundRecordDto) {
        FundRecord fundRecord = new FundRecord();
        fundRecord.setPostId(fundRecordDto.getPostId());
        fundRecord.setAmount(fundRecordDto.getAmount());
        fundRecord.setFundDate(fundRecordDto.getFundDate());
        fundRecord.setId(fundRecordDto.getId());
        fundRecordRepository.save(fundRecord);
    }


    @Override
    @Transactional(readOnly = true)
    public List<FundRecordDto> getDonationRecords(Long userId) {
        // userId를 사용하여 FundRecord 엔티티의 id 값과 일치하는 기록을 가져옴
        List<FundRecord> fundRecords = fundRecordRepository.findAllById(userId);

        List<FundRecordDto> fundRecordDtos = new ArrayList<>();
        for (FundRecord record : fundRecords) {
            Optional<FundPost> fundPostOpt = fundPostRepository.findById(record.getPostId());
            String title = fundPostOpt.map(FundPost::getTitle).orElse("제목 없음");

            Optional<Member> memberOpt = memberRepository.findById(record.getId());
            String username = memberOpt.map(Member::getUsername).orElse("익명");

            FundRecordDto dto = new FundRecordDto();
            dto.setFundId(record.getFundId());
            dto.setFundDate(record.getFundDate());
            dto.setAmount(record.getAmount());
            dto.setTitle(title);
            dto.setUsername(username);

            fundRecordDtos.add(dto);
        }
        return fundRecordDtos;
    }







}

