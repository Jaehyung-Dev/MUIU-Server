package com.bit.muiu.entity;

import com.bit.muiu.dto.DiaryDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "diarySeqGenerator",
        sequenceName = "DIARY_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "diarySeqGenerator"
    )
    private Long diary_id; // 다이어리의 고유 ID (Primary Key)

    // Many-to-One 관계 설정: 여러 다이어리는 하나의 사용자와 연결될 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "id", nullable = false)  // foreign key 설정
    private Member member; // Member 엔티티와의 관계

    @Column(length = 50, nullable = false)
    private String mood; // 기분

    @Column(length = 100, nullable = false)
    private String title; // 제목

    @Column(length = 2000, nullable = false)
    private String content; // 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regdate; // 등록 날짜 (자동 생성)

    @UpdateTimestamp
    private LocalDateTime moddate; // 수정 날짜 (자동 갱신)

    // Diary 엔티티를 DiaryDto로 변환
    public DiaryDto toDto() {
        return DiaryDto.builder()
                .diary_id(this.diary_id)
                .writer_id(this.member.getId())  // member의 id 가져오기
                .mood(this.mood)
                .title(this.title)
                .content(this.content)
                .regdate(this.regdate)
                .moddate(this.moddate)
                .build();
    }
}
