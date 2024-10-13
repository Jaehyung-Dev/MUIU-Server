package com.bit.muiu.entity;

import com.bit.muiu.dto.FundPostDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@SequenceGenerator(
        name = "fundPostSeqGenerator",
        sequenceName = "FUND_POST_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundPost {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fundPostSeqGenerator"
    )
    private Long postId;

    @Column(nullable = false)
    private Long id; // 회원 ID, 다른 테이블과 연결될 FK

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 5000)
    private String description;

    @Column(nullable = false)
    private Long targetAmount;

    private Long currentAmount;

    @Temporal(TemporalType.DATE)
    private Date fundStartDate;

    @Temporal(TemporalType.DATE)
    private Date fundEndDate;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    private Date modifiedAt;

    @Column(length = 15, nullable = false)
    private String teamName;

    @Column(length = 255)
    private String mainImage;

    public FundPostDto toDto() {
        return FundPostDto.builder()
                .postId(this.postId)
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .targetAmount(this.targetAmount)
                .currentAmount(this.currentAmount)
                .fundStartDate(this.fundStartDate)
                .fundEndDate(this.fundEndDate)
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .teamName(this.teamName)
                .mainImage(this.mainImage)
                .build();
    }
}
