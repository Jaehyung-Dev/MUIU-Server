package com.bit.muiu.service.impl;

import com.bit.muiu.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bit.muiu.dto.FundPostDto;
import com.bit.muiu.entity.FundPost;
import com.bit.muiu.repository.FundPostRepository;

@Service
public class FundServiceImpl implements FundService {

    private final FundPostRepository fundPostRepository;

    @Autowired
    public FundServiceImpl(FundPostRepository fundPostRepository) {
        this.fundPostRepository = fundPostRepository;
    }

    @Override
    public FundPostDto createFundPost(FundPostDto fundPostDto) {
        FundPost fundPost = fundPostDto.toEntity();
        fundPost = fundPostRepository.save(fundPost);
        return fundPost.toDto();
    }
}

