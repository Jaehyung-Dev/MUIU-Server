package com.bit.muiu.service.impl;

import com.bit.muiu.dto.DiaryDto;
import com.bit.muiu.entity.Diary;
import com.bit.muiu.repository.DiaryRepository;
import com.bit.muiu.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;

    @Override
    public DiaryDto writeDiary(DiaryDto diaryDto) {
        // Convert DTO to Entity
        Diary diary = diaryDto.toEntity();

        // Save the diary entry
        Diary savedDiary = diaryRepository.save(diary);

        // Convert Entity back to DTO and return
        return savedDiary.toDto();
    }
}
