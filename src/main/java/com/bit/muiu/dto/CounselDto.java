package com.bit.muiu.dto;

import com.bit.muiu.entity.Counsel;
import com.bit.muiu.entity.Member;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CounselDto {
    private Long counselId;
    private String type;
    private Date startTime;
    private Date endTime;
    private Long counsellorId;
    private Long clientId;

    public Counsel toEntity(Member counsellor, Member client) {
        return Counsel.builder()
                .counselId(this.counselId)
                .type(this.type)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .counsellorId(counsellor.getId())
                .clientId(client.getId())
                .build();
    }
}
