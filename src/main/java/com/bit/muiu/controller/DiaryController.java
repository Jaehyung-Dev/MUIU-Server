package com.bit.muiu.controller;

import com.bit.muiu.dto.DiaryDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.service.DiaryService;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.MemberRepository; // 추가된 부분
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
@Slf4j
public class DiaryController {

    private final DiaryService diaryService;
    private final MemberRepository memberRepository; // 추가: 회원 정보 조회를 위한 repository

    @PostMapping("/write")
    public ResponseEntity<?> writeDiary(@RequestBody DiaryDto diaryDto) {
        ResponseDto<DiaryDto> responseDto = new ResponseDto<>();

        try {
            // 현재 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // 로그인한 사용자 정보
            String username = userDetails.getUsername(); // username 가져오기

            // username을 통해 Member 엔티티에서 id를 가져옴
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

            Long memberId = member.getId();  // Member의 id 값 가져오기
            log.info("Writing diary for member ID: {}", memberId);

            // 오늘의 일기가 이미 존재하는지 확인
            Optional<DiaryDto> existingDiaryOpt = diaryService.getTodayDiary(memberId);

            if (existingDiaryOpt.isPresent()) {
                // 기존 일기가 존재하는 경우, regdate만 업데이트하고 저장
                DiaryDto existingDiary = existingDiaryOpt.get();
                existingDiary.setTitle(diaryDto.getTitle());
                existingDiary.setContent(diaryDto.getContent());
                existingDiary.setMood(diaryDto.getMood());
                existingDiary.setRegdate(LocalDateTime.now()); // regdate만 현재 시각으로 업데이트
                DiaryDto updatedDiary = diaryService.updateDiary(existingDiary);

                responseDto.setStatusCode(HttpStatus.OK.value());
                responseDto.setStatusMessage("기존 일기가 업데이트되었습니다.");
                responseDto.setItem(updatedDiary);
                return ResponseEntity.ok(responseDto);
            }

            // 일기가 없을 경우 새로운 일기 작성
            diaryDto.setWriter_id(memberId);
            DiaryDto savedDiary = diaryService.writeDiary(diaryDto);

            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("Diary created successfully");
            responseDto.setItem(savedDiary);

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            log.error("Error while writing diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDto.setStatusMessage("Invalid user or data: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            log.error("Error while writing diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/user/{writerId}/latest")
    public ResponseEntity<?> getLatestDiaryByWriterId(@PathVariable Long writerId) {
        ResponseDto<DiaryDto> responseDto = new ResponseDto<>();

        try {
            // 현재 로그인된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

            if (!member.getId().equals(writerId)) {
                throw new IllegalArgumentException("접근 권한이 없습니다.");
            }

            log.info("Fetching latest diary for writer ID: {}", writerId);

            // 최신 일기 하나 조회
            Optional<DiaryDto> latestDiary = diaryService.getLatestDiaryByWriterId(writerId);

            if (latestDiary.isPresent()) {
                log.info("Latest diary found: {}", latestDiary.get()); // 최신 일기 내용 출력
                responseDto.setStatusCode(HttpStatus.OK.value());
                responseDto.setStatusMessage("가장 최신 일기 조회 성공");
                responseDto.setItem(latestDiary.get());
                return ResponseEntity.ok(responseDto);
            } else {
                log.warn("No diary found for writer ID: {}", writerId); // 일기 없음 경고
                responseDto.setStatusCode(HttpStatus.NOT_FOUND.value());
                responseDto.setStatusMessage("일기를 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
            }
        } catch (IllegalArgumentException e) {
            log.error("Error while fetching latest diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDto.setStatusMessage("Invalid user or data: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            log.error("Error while fetching latest diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<?> deleteDiary(@PathVariable Long diaryId) {
        ResponseDto<Void> responseDto = new ResponseDto<>();

        try {
            // 현재 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // username을 통해 Member 엔티티에서 id를 가져옴
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

            // 해당 일기의 작성자가 현재 사용자와 일치하는지 확인
            if (!diaryService.isDiaryOwner(diaryId, member.getId())) {
                responseDto.setStatusCode(HttpStatus.FORBIDDEN.value());
                responseDto.setStatusMessage("삭제 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
            }

            // 일기 삭제 로직 호출
            diaryService.deleteDiary(diaryId);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Diary deleted successfully");

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            log.error("Error while deleting diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDto.setStatusMessage("Invalid user or data: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            log.error("Error while deleting diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }
    // 사용자의 모든 일기를 가져오는 메서드 추가
    @GetMapping("/user")
    public ResponseEntity<?> getDiariesByUser() {
        ResponseDto<List<DiaryDto>> responseDto = new ResponseDto<>();

        try {
            // 현재 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // username을 통해 Member 엔티티에서 id를 가져옴
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

            Long memberId = member.getId();

            // 사용자 일기 목록 조회
            List<DiaryDto> diaries = diaryService.getDiariesByWriterId(memberId);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("사용자의 모든 일기 조회 성공");
            responseDto.setItem(diaries);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error while fetching user diaries: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<?> updateDiary(@PathVariable Long diaryId, @RequestBody DiaryDto diaryDto) {
        ResponseDto<DiaryDto> responseDto = new ResponseDto<>();

        try {
            // 현재 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // username을 통해 Member 엔티티에서 id를 가져옴
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

            // 해당 일기의 작성자가 현재 사용자와 일치하는지 확인
            if (!diaryService.isDiaryOwner(diaryId, member.getId())) {
                responseDto.setStatusCode(HttpStatus.FORBIDDEN.value());
                responseDto.setStatusMessage("수정 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
            }

            // 일기 업데이트 로직 호출
            diaryDto.setDiary_id(diaryId); // 기존 일기 ID 설정
            DiaryDto updatedDiary = diaryService.updateDiary(diaryDto);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Diary updated successfully");
            responseDto.setItem(updatedDiary);

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            log.error("Error while updating diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDto.setStatusMessage("Invalid user or data: " + e.getMessage());
            return ResponseEntity.badRequest().body(responseDto);
        } catch (Exception e) {
            log.error("Error while updating diary: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("Internal server error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

}

