package com.bit.muiu.dto;

import com.bit.muiu.entity.FundPost;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FundPostDto {
    private Long postId;
    private Long id;
    private String title;
    private String description;
    private Long targetAmount;
    private Long currentAmount;
    private Date fundStartDate;
    private Date fundEndDate;
    private Date createdAt;
    private Date modifiedAt;
    private String teamName;
    private String mainImage;

    public FundPost toEntity() {
        return FundPost.builder()
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
