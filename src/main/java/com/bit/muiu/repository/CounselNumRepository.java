package com.bit.muiu.repository;

import com.bit.muiu.entity.CounselNum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounselNumRepository extends JpaRepository<CounselNum, Integer> {
    Optional<CounselNum> findByAuthNum(String authNum);
}
