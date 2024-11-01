package com.bit.muiu.service;

import com.bit.muiu.dto.MindColumnDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MindColumnService {
    Page<MindColumnDto> post(MindColumnDto mindColumnDto, MultipartFile[] uploadFiles, Pageable pageable);

    Page<MindColumnDto> findAll(Pageable pageable);

    Page<MindColumnDto> modify(MindColumnDto mindColumnDto, List<MultipartFile> mcfList, List<Long> deletedFileIds, Pageable pageable);

    void deleteById(long id);
}
