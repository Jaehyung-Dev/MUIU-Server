package com.bit.muiu.dto;

import com.bit.muiu.entity.Diary;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DiaryDto {
    private Long id;
    private Long diaryId;
    private String mood;
    private String title;
    private String content;
    private LocalDateTime regdate;
    private LocalDateTime moddate;

    public Diary toEntity() {
        return Diary.builder()
                .id(this.id)
//                .diaryId(this.diaryId)
                .mood(this.mood)
                .title(this.title)
                .content(this.content)
//                .regdate(this.regdate)
//                .moddate(this.moddate)
                .build();
    }
}
