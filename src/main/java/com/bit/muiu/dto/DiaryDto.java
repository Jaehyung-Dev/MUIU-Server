package com.bit.muiu.dto;

import com.bit.muiu.entity.Diary;
import com.bit.muiu.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DiaryDto {
    private Long id; // Member의 id
    private Long diary_id;
    private String mood;
    private String title;
    private String content;
    private LocalDateTime regdate;
    private LocalDateTime moddate;

    private Member member; // Member 객체 추가

    public Diary toEntity() {
        return Diary.builder()
                .member(this.member)  // Member 객체 설정
                .mood(this.mood)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
