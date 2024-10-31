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
    private Long id;
    private Long counselId;
    private String type;
    private Long counselorId;
    private Long clientId;

    public Counsel toEntity(Member counselor, Member client) {
        return Counsel.builder()
                .id(this.id)
                .counselId(this.counselId)
                .type(this.type)
                .counselorId(counselor.getId())
                .clientId(client.getId())
                .build();
    }
}
