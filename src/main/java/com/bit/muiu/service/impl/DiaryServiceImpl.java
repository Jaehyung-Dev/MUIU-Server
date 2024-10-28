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

import java.util.List;
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

    public List<DiaryDto> getDiariesByWriterId(Long writerId) {
        List<Diary> diaries = diaryRepository.findByMemberId(writerId);
        return diaries.stream()
                .map(DiaryDto::fromEntity)  // Diary 엔티티를 DiaryDto로 변환
                .collect(Collectors.toList());
    }
}
