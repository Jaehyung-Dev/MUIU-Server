package com.bit.muiu.dto;

import com.bit.muiu.entity.AIChat;
import com.bit.muiu.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AIChatDto {
    private Long ac_id;
    private Long user_id;
    private String user_name;
    private LocalDateTime ac_regdate;

    public AIChat toEntity(Member member) {
        return AIChat.builder()
                .ac_id(this.ac_id)
                .member(member)
                .ac_regdate(this.ac_regdate)
                .build();
    }
}
