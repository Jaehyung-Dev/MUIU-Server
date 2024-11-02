package com.bit.muiu.call;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "call_room")
public class CallRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_number")
    private long roomNumber;

    @Column(name = "counselor_id", nullable = false)
    private Long counselorId;

    @Column(name = "client_id", nullable = true)
    private Long clientId;

    @Column(name = "room_status", nullable = false)
    private String roomStatus = "WAITING";

    public CallRoom(Long counselorId) {
        this.counselorId = counselorId;
        this.roomStatus = "WAITING";
    }

    public CallRoom() {

    }
}
