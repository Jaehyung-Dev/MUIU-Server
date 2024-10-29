package com.bit.muiu.repository;

import com.bit.muiu.entity.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    long countByUsername(String username);

    Optional<Member> findFirstByStatusAndIdNot(String waiting, Long userId);

    @Query("SELECT m FROM Member m WHERE m.status = :status AND m.role <> :role")
    List<Member> findByStatusAndDifferentRole(@Param("status") String status, @Param("role") String role);
}
