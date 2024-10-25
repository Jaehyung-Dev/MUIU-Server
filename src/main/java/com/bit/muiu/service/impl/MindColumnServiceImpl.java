package com.bit.muiu.service.impl;

import com.bit.muiu.common.FileUtils;
import com.bit.muiu.dto.MindColumnDto;
import com.bit.muiu.dto.MindColumnFileDto;
import com.bit.muiu.entity.MindColumn;
import com.bit.muiu.repository.MindColumnFileRepository;
import com.bit.muiu.repository.MindColumnRepository;
import com.bit.muiu.service.MindColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class MindColumnServiceImpl implements MindColumnService {
    private final MindColumnRepository mindColumnRepository;
    private final MindColumnFileRepository mindColumnFileRepository;
    private final FileUtils fileUtils;

    @Override
    public Page<MindColumnDto> post(MindColumnDto mindColumnDto, MultipartFile[] uploadFiles, Pageable pageable) {
        mindColumnDto.setMc_img_num((long)uploadFiles.length);

        MindColumn mindColumn = mindColumnDto.toEntity();

        if (uploadFiles != null) {
            Arrays.stream(uploadFiles).forEach(multipartFile -> {
                if(multipartFile.getOriginalFilename() != null &&
                        !multipartFile.getOriginalFilename().equalsIgnoreCase("")) {

                    MindColumnFileDto mindColumnFileDto = fileUtils.parserFileInfo(multipartFile, "mindColumn/");

                    mindColumn.getMcfList().add(mindColumnFileDto.toEntity(mindColumn));
                }
            });
        }

        mindColumnRepository.save(mindColumn);

        return mindColumnRepository.findAll(pageable).map(MindColumn::toDto);
    }

    @Override
    public Page<MindColumnDto> findAll(Pageable pageable) {
        return mindColumnRepository.findAll(pageable).map(MindColumn::toDto);
    }

    @Override
    public MindColumnDto findById(Long id) {
        return mindColumnRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("column is not exist"))
                .toDto();
    }


}
