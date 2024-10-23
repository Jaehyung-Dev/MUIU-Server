package com.bit.muiu.repository;

import com.bit.muiu.entity.DisasterMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisasterMessageRepository extends JpaRepository<DisasterMessage, Long> {
    List<DisasterMessage> findByCategory(String category);
}
