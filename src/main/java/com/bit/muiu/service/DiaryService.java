package com.bit.muiu.service;

import com.bit.muiu.dto.DiaryDto;

import java.util.List;
import java.util.Optional;

public interface DiaryService {
    DiaryDto writeDiary(DiaryDto diaryDto);

    List<DiaryDto> getDiariesByWriterId(Long memberId);

    DiaryDto getDiaryByWriterId(Long id);

    Optional<DiaryDto> getLatestDiaryByWriterId(Long writerId); // 최신 일기 하나를 가져오는 메서드 추가

    // 오늘 일기 작성 여부 확인
    boolean hasDiaryForToday(Long memberId);

    boolean isDiaryOwner(Long diaryId, Long memberId);

    void deleteDiary(Long diaryId);
}
