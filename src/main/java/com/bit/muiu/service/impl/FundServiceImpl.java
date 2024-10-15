package com.bit.muiu.service.impl;

import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.entity.FundPost;
import com.bit.muiu.repository.FundPostRepository;
import com.bit.muiu.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundPostRepository fundPostRepository;

    @Override
    public FundPostDto createFundPost(FundPostDto fundPostDto) {
        FundPost fundPost = fundPostDto.toEntity(); // DTO를 엔티티로 변환
        FundPost savedFundPost = fundPostRepository.save(fundPost); // DB에 저장
        return savedFundPost.toDto(); // 저장된 엔티티를 다시 DTO로 변환하여 반환
    }
}
