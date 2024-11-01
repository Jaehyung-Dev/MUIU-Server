package com.bit.muiu.repository;

import com.bit.muiu.entity.FundRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FundRecordRepository extends JpaRepository<FundRecord, Long> {
    List<FundRecord> findAllById(Long id);
}
