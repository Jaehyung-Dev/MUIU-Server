package com.bit.muiu.dto;

import com.bit.muiu.entity.MindColumn;
import com.bit.muiu.entity.MindColumnFile;
import lombok.*;

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
    private String mcf_path;
    private String mcf_status;
    private String newfilename;

    public MindColumnFile toEntity(MindColumn mindColumn) {
        return MindColumnFile.builder()
                .mcf_id(this.mcf_id)
                .mindColumn(mindColumn)
                .mcf_name(this.mcf_name)
                .mcf_originname(this.mcf_originname)
                .mcf_path(this.mcf_path)
                .mcf_status(this.mcf_status)
                .newfilename(this.newfilename)
                .build();
    }
}
