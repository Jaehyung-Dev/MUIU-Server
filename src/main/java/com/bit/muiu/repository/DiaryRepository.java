package com.bit.muiu.repository;

import com.bit.muiu.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query(value = "SELECT * FROM diary WHERE writer_id = :writerId ORDER BY regdate DESC, diary_id DESC LIMIT 1", nativeQuery = true)
    Optional<Diary> findTopByMemberIdOrderByRegdateDesc(@Param("writerId") Long writerId);

    List<Diary> findByMemberId(Long writerId);

}
