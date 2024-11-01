package com.bit.muiu.repository;

import com.bit.muiu.entity.FundRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FundRecordRepository extends JpaRepository<FundRecord, Long> {
    List<FundRecord> findAllById(Long id);

    @Query("SELECT SUM(fr.amount) FROM FundRecord fr WHERE fr.postId = :postId")
    Long sumAmountByPostId(@Param("postId") Long postId);

}
