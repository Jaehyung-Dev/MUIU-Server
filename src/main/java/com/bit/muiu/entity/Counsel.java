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
    private Long id;

    @Column(nullable = false)
    private Long counselId;

    @Column(length = 20, nullable = false)
    private String type;

    @Column(nullable = false)
    private Long counselorId;

    @Column(nullable = true)
    private Long clientId;
}
