package com.bit.muiu.service;

import com.bit.muiu.dto.FundPostDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FundService {
    FundPostDto createFundPost(FundPostDto fundPostDto, MultipartFile file);

    List<FundPostDto> getAllPosts();

    FundPostDto getPostById(Long id);
}
