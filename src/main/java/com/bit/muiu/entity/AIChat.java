package com.bit.muiu.entity;

import com.bit.muiu.dto.AIChatDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "aiChatSeqGenerator",
        sequenceName = "AI_CHAT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIChat {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "aiChatSeqGenerator"
    )
    private Long ac_id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private Member member;

    private LocalDateTime ac_regdate;

    public AIChatDto toDto(){
        return AIChatDto.builder()
                .ac_id(this.ac_id)
                .user_id(this.member.getId())
                .user_name(this.member.getName())
                .ac_regdate(this.ac_regdate)
                .build();
    }
}
