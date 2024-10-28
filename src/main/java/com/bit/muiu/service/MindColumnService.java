package com.bit.muiu.service;

import com.bit.muiu.dto.MindColumnDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MindColumnService {
    Page<MindColumnDto> post(MindColumnDto mindColumnDto, MultipartFile[] uploadFiles, Pageable pageable);

    Page<MindColumnDto> findAll(Pageable pageable);

    MindColumnDto findById(Long id);
}
