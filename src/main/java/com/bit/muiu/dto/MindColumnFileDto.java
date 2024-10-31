package com.bit.muiu.dto;

import com.bit.muiu.entity.MindColumn;
import com.bit.muiu.entity.MindColumnFile;
import lombok.*;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MindColumnFileDto {
    private Long mcf_id;
    private Long mc_id;
    private String mcf_name;
    private String mcf_originname;
    private int order_index;
    private String mcf_path;
    @Transient
    private Boolean isNew;

    public MindColumnFile toEntity(MindColumn mindColumn) {
        return MindColumnFile.builder()
                .mcf_id(this.mcf_id)
                .mindColumn(mindColumn)
                .mcf_name(this.mcf_name)
                .mcf_originname(this.mcf_originname)
                .order_index(this.order_index)
                .mcf_path(this.mcf_path)
                .isNew(this.isNew)
                .build();
    }
}
