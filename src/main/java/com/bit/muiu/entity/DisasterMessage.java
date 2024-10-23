package com.bit.muiu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "disaster_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisasterMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "alert_level", nullable = false)
    private String alertLevel;

    @Column(name = "event_content", nullable = false, columnDefinition = "TEXT")
    private String eventContent;

    @Column(name = "occurrence_time", nullable = false)
    private String occurrenceTime;

    @Column(name = "read_status", nullable = false)
    private String readStatus;

    @Column(name = "message_content", nullable = false, columnDefinition = "TEXT")
    private String messageContent;
}