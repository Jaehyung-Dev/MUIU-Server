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
    private Long writer_id; // Member의 id
    private Long diary_id;
    private String mood;
    private String title;
    private String content;
    private LocalDateTime regdate;
    private LocalDateTime moddate;

    // Diary 엔티티를 DiaryDto로 변환하는 메서드
    public static DiaryDto fromEntity(Diary diary) {
        return DiaryDto.builder()
                .diary_id(diary.getDiary_id())
                .writer_id(diary.getMember().getId())  // Member의 ID
                .mood(diary.getMood())
                .title(diary.getTitle())
                .content(diary.getContent())
                .regdate(diary.getRegdate())
                .moddate(diary.getModdate())
                .build();
    }
    // DiaryDto를 Diary 엔티티로 변환하는 메서드
    public Diary toEntity(Member member) {
        return Diary.builder()
                .diary_id(this.diary_id)       // 기존 ID가 있을 경우 설정
                .member(member)                // Member 설정
                .mood(this.mood)
                .title(this.title)
                .content(this.content)
                .regdate(this.regdate)         // 등록일 설정
                .moddate(this.moddate)         // 수정일 설정
                .build();
    }
}
