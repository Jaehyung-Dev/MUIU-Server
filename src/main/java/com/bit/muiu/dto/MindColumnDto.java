package com.bit.muiu.dto;

import com.bit.muiu.entity.MindColumn;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MindColumnDto {
    private Long mc_id;
    private String mc_title;
    private Long mc_img_num;
    private List<MindColumnFileDto> mcfList;

    public MindColumn toEntity() {
        return MindColumn.builder()
                .mc_id(this.mc_id)
                .mc_title(this.mc_title)
                .mc_img_num(this.mc_img_num)
                .mcfList(new ArrayList<>())
                .build();
    }
}
