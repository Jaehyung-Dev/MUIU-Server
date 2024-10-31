package com.bit.muiu.repository;

import com.bit.muiu.entity.CallRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRoomRepository  extends JpaRepository<CallRoom, Long> {
}
