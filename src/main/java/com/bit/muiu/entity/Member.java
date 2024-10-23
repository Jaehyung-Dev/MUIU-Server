package com.bit.muiu.entity;

import com.bit.muiu.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private Date birth;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String tel;

    @Column(nullable = true)
    private Boolean locationAgree;

    private String role;
    private Boolean recordConsent;

    @Column(nullable = true)
    private String profileImageUrl;

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(this.id)
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .name(this.name)
                .birth(this.birth)
                .gender(this.gender)
                .tel(this.tel)
                .role(this.role)
                .locationAgree(this.locationAgree)
                .recordConsent(this.recordConsent)
                .profileImageUrl(this.profileImageUrl)
                .build();
    }
}
