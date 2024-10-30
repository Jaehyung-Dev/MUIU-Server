package com.bit.muiu.repository;

import com.bit.muiu.entity.CallRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallRoomRepository extends JpaRepository<CallRoom, Long> {
    // 추가적인 쿼리 메서드를 정의할 수 있습니다.
}
