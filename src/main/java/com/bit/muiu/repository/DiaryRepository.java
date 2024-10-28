package com.bit.muiu.repository;

import com.bit.muiu.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByMemberId(Long writerId);
}
