package com.bit.muiu.repository;

import com.bit.muiu.dto.DiaryDto;
import com.bit.muiu.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query(value = "SELECT * FROM diary WHERE writer_id = :writerId ORDER BY regdate DESC, diary_id DESC LIMIT 1", nativeQuery = true)
    Optional<Diary> findTopByMemberIdOrderByRegdateDesc(@Param("writerId") Long writerId);

    List<Diary> findByMemberId(Long writerId);

    boolean existsByMemberIdAndRegdateBetween(Long memberId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT COUNT(d) > 0 FROM Diary d WHERE d.diary_id = :diaryId AND d.member.id = :memberId")
    boolean existsByDiaryIdAndMemberId(@Param("diaryId") Long diaryId, @Param("memberId") Long memberId);

    Optional<Diary> findByMemberIdAndRegdateBetween(Long memberId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
