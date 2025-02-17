package com.bit.muiu.service.impl;

import com.bit.muiu.dto.DiaryDto;
import com.bit.muiu.entity.Diary;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.DiaryRepository;
import com.bit.muiu.repository.MemberRepository;
import com.bit.muiu.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository; // memberRepository를 사용해서 Member를 조회

    @Override
    public DiaryDto writeDiary(DiaryDto diaryDto) {
        // 현재 로그인된 사용자 정보를 SecurityContext에서 가져오기
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Member 엔티티를 username을 이용해 찾아오기
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

        // Convert DTO to Entity
        Diary diary = diaryDto.toEntity(member);

        // Diary 엔티티에 사용자 정보 설정
        diary.setMember(member);  // `member`와 연결

        // Save the diary entry
        Diary savedDiary = diaryRepository.save(diary);

        // Convert Entity back to DTO and return
        return savedDiary.toDto();
    }

    @Override
    public List<DiaryDto> getDiariesByWriterId(Long writerId) {
        List<Diary> diaries = diaryRepository.findByMemberId(writerId);
        return diaries.stream()
                .map(DiaryDto::fromEntity)  // Diary 엔티티를 DiaryDto로 변환
                .collect(Collectors.toList());
    }

    @Override
    public DiaryDto getDiaryByWriterId(Long id) {
        // 다이어리 ID로 다이어리 엔티티를 조회
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 일기를 찾을 수 없습니다: " + id));

        // 엔티티를 DTO로 변환하여 반환
        return diary.toDto();
    }

    @Override
    public Optional<DiaryDto> getLatestDiaryByWriterId(Long writerId) {
        // writerId에 따른 최신 일기 하나만 조회
        return diaryRepository.findTopByMemberIdOrderByRegdateDesc(writerId)
                .map(DiaryDto::fromEntity);
    }

    @Override
    public boolean hasDiaryForToday(Long memberId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return diaryRepository.existsByMemberIdAndRegdateBetween(memberId, startOfDay, endOfDay);
    }

    @Override
    public boolean isDiaryOwner(Long diaryId, Long memberId) {
        return diaryRepository.existsByDiaryIdAndMemberId(diaryId, memberId);
    }

    @Override
    public void deleteDiary(Long diaryId) {
        if (diaryRepository.existsById(diaryId)) {
            diaryRepository.deleteById(diaryId);
        } else {
            throw new IllegalArgumentException("Diary not found");
        }
    }

    @Override
    public Optional<DiaryDto> getTodayDiary(Long memberId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return diaryRepository.findByMemberIdAndRegdateBetween(memberId, startOfDay, endOfDay)
                .map(DiaryDto::fromEntity); // Diary 엔티티를 DiaryDto로 변환하여 반환
    }

    @Override
    public DiaryDto updateDiary(DiaryDto diaryDto) {
        Diary existingDiary = diaryRepository.findById(diaryDto.getDiary_id())
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));

        existingDiary.setTitle(diaryDto.getTitle());
        existingDiary.setContent(diaryDto.getContent());
        existingDiary.setMood(diaryDto.getMood());
        existingDiary.setModdate(LocalDateTime.now()); // 수정일만 업데이트

        Diary updatedDiary = diaryRepository.save(existingDiary); // 엔티티 저장
        return DiaryDto.fromEntity(updatedDiary); // 저장된 엔티티를 DiaryDto로 변환하여 반환
    }

    @Override
    public List<Integer> getWeekEmotionDataByWriterId(Long writerId) {
        // 현재 날짜를 기준으로 이번 주 월요일부터 오늘까지 범위 설정
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfDay = today.plusDays(1); // 오늘의 마지막 시간까지 포함

        LocalDateTime startOfWeekTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfTodayTime = endOfDay.atStartOfDay();

        List<Diary> weeklyDiaries = diaryRepository.findByMemberId(writerId);

        // 요일별 감정 데이터 초기화 (월 ~ 오늘까지)
        List<Integer> emotionData = startOfWeek.datesUntil(today.plusDays(1))
                .map(date -> {
                    Optional<Diary> diaryForDate = weeklyDiaries.stream()
                            .filter(diary -> diary.getRegdate().toLocalDate().equals(date)) // 특정 날짜의 일기 필터링
                            .findFirst();
                    return diaryForDate.map(diary -> moodToValue(diary.getMood())) // 일기 감정 변환
                            .orElse(0); // 일기 없는 날은 0으로 설정
                })
                .collect(Collectors.toList());

        return emotionData;
    }

    @Override
    public List<Integer> getMonthEmotionDataByWriterId(Long writerId) {
        LocalDate today = LocalDate.now().minusMonths(1).withDayOfMonth(28); // 테스트를 위해 지난달 설정
        LocalDate startOfMonth = today.withDayOfMonth(1); // 이번 달의 첫날

        // 이번 달의 일기 데이터를 가져옴
        List<Diary> monthlyDiaries = diaryRepository.findByMemberId(writerId);

        // 주별 평균 감정 데이터를 담을 리스트
        List<Integer> weeklyAverages = new ArrayList<>();

        // 이번 달의 각 주를 반복
        LocalDate weekStart = startOfMonth;
        while (weekStart.isBefore(today.plusDays(1))) {
            // 람다 내에서 사용할 effectively final 변수를 새로 정의
            LocalDate currentWeekStart = weekStart;
            LocalDate weekEnd = weekStart.plusDays(6);
            LocalDate currentWeekEnd = weekEnd;

            // 해당 주차에 속하는 일기 데이터를 필터링
            List<Integer> weekData = monthlyDiaries.stream()
                    .filter(diary -> {
                        LocalDate diaryDate = diary.getRegdate().toLocalDate();
                        return (diaryDate.isEqual(currentWeekStart) || diaryDate.isAfter(currentWeekStart)) &&
                                (diaryDate.isEqual(currentWeekEnd) || diaryDate.isBefore(currentWeekEnd));
                    })
                    .map(diary -> moodToValue(diary.getMood())) // 감정 값을 숫자로 변환
                    .collect(Collectors.toList());

            // 해당 주의 평균 감정 값을 계산
            if (!weekData.isEmpty()) {
                int weeklySum = weekData.stream().mapToInt(Integer::intValue).sum();
                int weeklyAverage = Math.round((float) weeklySum / weekData.size()); // 반올림하여 정수로 변환
                weeklyAverages.add(weeklyAverage);
            } else {
                weeklyAverages.add(0); // 데이터가 없는 주는 0으로 설정
            }

            // 다음 주차로 이동
            weekStart = weekStart.plusDays(7);
        }

        return weeklyAverages;
    }


    private int moodToValue(String mood) {
        switch (mood) {
            case "dissatisfied":
                return 1;
            case "bad":
                return 2;
            case "soso":
                return 3;
            case "good":
                return 4;
            case "happy":
                return 5;
            default:
                return 0;
        }
    }
    @Override
    public List<DiaryDto> searchDiariesByQuery(Long memberId, String query) {
        List<Diary> diaries = diaryRepository.searchDiariesByQuery(memberId, query);
        return diaries.stream()
                .map(DiaryDto::fromEntity)
                .collect(Collectors.toList());
    }
}
