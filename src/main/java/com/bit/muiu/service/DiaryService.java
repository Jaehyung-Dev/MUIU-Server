package com.bit.muiu.service;

import com.bit.muiu.dto.DiaryDto;

import java.util.List;

public interface DiaryService {
    DiaryDto writeDiary(DiaryDto diaryDto);

    List<DiaryDto> getDiariesByWriterId(Long memberId);
}
