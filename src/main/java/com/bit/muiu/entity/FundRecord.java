package com.bit.muiu.entity;

import com.bit.muiu.dto.FundRecordDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@SequenceGenerator(
        name = "fundRecordSeqGenerator",
        sequenceName = "FUND_RECORD_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundRecord {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fundRecordSeqGenerator"
    )
    private Long fundId;

    @Column(nullable = false)
    private Long id; // 회원 ID, 다른 테이블과 연결될 FK

    @Column(nullable = false)
    private Long postId; // 기부 게시글 ID, FundPost 엔티티와 연결될 FK

    @Column(nullable = false)
    private Long amount; // 기부 금액

    @Temporal(TemporalType.DATE)
    private Date fundDate; // 기부 날짜

    public FundRecordDto toDto() {
        return FundRecordDto.builder()
                .fundId(this.fundId)
                .id(this.id)
                .postId(this.postId)
                .amount(this.amount)
                .fundDate(this.fundDate)
                .build();
    }
}
