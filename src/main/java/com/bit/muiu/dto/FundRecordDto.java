package com.bit.muiu.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FundRecordDto {
    private Long fundId;
    private Long id; // 회원 ID
    private Long postId; // 기부 게시글 ID
    private Long amount; // 기부 금액
    private Date fundDate; // 기부 날짜
    private String title;
    private String username;
}
