package com.bit.muiu.service.impl;

import com.bit.muiu.common.FileUtils;
import com.bit.muiu.dto.MindColumnDto;
import com.bit.muiu.dto.MindColumnFileDto;
import com.bit.muiu.entity.MindColumn;
import com.bit.muiu.entity.MindColumnFile;
import com.bit.muiu.repository.MindColumnFileRepository;
import com.bit.muiu.repository.MindColumnRepository;
import com.bit.muiu.service.MindColumnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        mindColumnDto.setRegdate(LocalDateTime.now());

        MindColumn mindColumn = mindColumnDto.toEntity();

        if (uploadFiles != null) {
            AtomicInteger order = new AtomicInteger(1);
            Arrays.stream(uploadFiles).forEach(multipartFile -> {
                if(multipartFile.getOriginalFilename() != null &&
                        !multipartFile.getOriginalFilename().equalsIgnoreCase("")) {

                    MindColumnFileDto mindColumnFileDto = fileUtils.parserFileInfo(multipartFile, "mindColumn/");
                    mindColumnFileDto.setOrder_index(order.getAndIncrement());

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
    public Page<MindColumnDto> modify(MindColumnDto mindColumnDto, List<MultipartFile> mcfList, List<Long> deletedFileIds, Pageable pageable) {
        MindColumn mindColumn = mindColumnRepository.findById(mindColumnDto.getMc_id()).orElseThrow(
                () -> new RuntimeException("columns is not exist")
        );
        mindColumn.setMc_title(mindColumnDto.getMc_title());

        if(deletedFileIds != null && !deletedFileIds.isEmpty()){
            for(Long fileId : deletedFileIds){
                mindColumnFileRepository.findById(fileId).ifPresent(file -> {
                    fileUtils.deleteFile(file.getMcf_path(), file.getMcf_name());
                    mindColumnFileRepository.delete(file);
                });
            }
        }

        List<MindColumnFile> updatedFiles = new ArrayList<>();
        int order = 1;

        for(MindColumnFileDto mindColumnFileDto : mindColumnDto.getMcfList()){
            if(mindColumnFileDto.getIsNew()) {
                MultipartFile file = mcfList.stream()
                        .filter(f -> f.getOriginalFilename().equals(mindColumnFileDto.getMcf_originname()))
                        .findFirst().orElse(null);

                if(file != null){
                    MindColumnFile mindColumnFile = fileUtils.parserFileInfo(file, "mindColumn/").toEntity(mindColumn);
                    mindColumnFile.setOrder_index(order++);
                    updatedFiles.add(mindColumnFile);
                }
            } else {
                MindColumnFile mindColumnFile = mindColumnFileRepository.findById(mindColumnFileDto.getMc_id()).orElseThrow(
                        () -> new RuntimeException("file is not exist")
                );
                mindColumnFile.setOrder_index(order++);
                updatedFiles.add(mindColumnFile);

            }
        }

        mindColumn.setMcfList(updatedFiles);
        mindColumnRepository.save(mindColumn);

        return mindColumnRepository.findAll(pageable).map(MindColumn::toDto);
    }

    @Override
    public void deleteById(long id) {
        // 버킷에서도 사진 지우는거 구현 필요
        mindColumnRepository.deleteById(id);
    }


}
