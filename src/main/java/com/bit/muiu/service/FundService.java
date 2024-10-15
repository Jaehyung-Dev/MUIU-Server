package com.bit.muiu.service;

import com.bit.muiu.dto.FundPostDto;
import org.springframework.web.multipart.MultipartFile;

public interface FundService {
    FundPostDto createFundPost(FundPostDto fundPostDto, MultipartFile file);
}
