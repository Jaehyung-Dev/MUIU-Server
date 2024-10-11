package com.bit.muiu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@SequenceGenerator(
        name = "counselSeqGenerator",
        sequenceName = "COUNSEL_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counsel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "counselSeqGenerator"
    )
    private Long counselId;

    @Column(length = 20, nullable = false)
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endTime;

    @Column(nullable = false)
    private Long counsellorId;

    @Column(nullable = false)
    private Long clientId;

    public void assignMembers(Member counsellor, Member client) {
        if (!"counsellor".equals(counsellor.getRole())) {
            throw new IllegalArgumentException("The role of the counsellor must be 'counsellor'.");
        }
        if (!"user".equals(client.getRole())) {
            throw new IllegalArgumentException("The role of the client must be 'user'.");
        }

        this.counsellorId = counsellor.getId();
        this.clientId = client.getId();
    }

}
