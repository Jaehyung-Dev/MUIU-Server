package com.bit.muiu.service;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.dto.FundRecordDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FundService {
    FundPostDto createFundPost(FundPostDto fundPostDto);

    List<FundPostDto> getAllPosts();

    FundPostDto getPostById(Long id);

    void updateFundPost(Long postId, FundPostDto fundPostDto);

    String uploadImage(MultipartFile file);

    void deleteFundPost(Long postId);

    @Transactional
    void savePaymentRecord(FundRecordDto fundRecordDto);

    @Transactional(readOnly = true)
    List<FundRecordDto> getDonationRecords(Long userId);

    void updateCurrentAmountForPost(Long postId);
}
