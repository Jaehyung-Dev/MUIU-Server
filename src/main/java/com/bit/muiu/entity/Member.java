package com.bit.muiu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;

@Entity
@SequenceGenerator(
        name = "memberSeqGenerator",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "memberSeqGenerator"
    )
    private Long id;
    private String username;
    private String password;
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Date birth;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String tel;
    @Column(nullable = false)
    private Boolean locBool;
    private String role;
    private Boolean record_consent;

}
