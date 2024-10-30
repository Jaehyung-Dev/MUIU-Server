package com.bit.muiu.repository;

import com.bit.muiu.entity.ChatRoom;
import com.bit.muiu.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
//    Optional<ChatRoom> findByUser1OrUser2(Member user1, Member user2);
    Optional<ChatRoom> findFirstByStatusOrderByIdAsc(String status);
    Optional<ChatRoom> findByClientIdOrCounselorId(Long clientId, Long counselorId);
}
