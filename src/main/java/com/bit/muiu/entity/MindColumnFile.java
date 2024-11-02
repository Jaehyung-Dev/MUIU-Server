package com.bit.muiu.entity;

import com.bit.muiu.dto.MindColumnFileDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "mindColumnFileSeqGenerator",
        sequenceName = "MIND_COLUMN_File_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MindColumnFile {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mindColumnFileSeqGenerator"
    )
    private Long mcf_id;

    @ManyToOne
    @JoinColumn(name = "mc_id", referencedColumnName = "mc_id")
    @JsonBackReference
    private MindColumn mindColumn;

    private String mcf_name;
    private String mcf_originname;
    private int order_index;
    private String mcf_path;
    @Transient
    private Boolean isNew;

    public MindColumnFileDto toDto() {
        return MindColumnFileDto.builder()
                .mcf_id(this.mcf_id)
                .mc_id(this.mindColumn.getMc_id())
                .mcf_name(this.mcf_name)
                .mcf_originname(this.mcf_originname)
                .order_index(this.order_index)
                .mcf_path(this.mcf_path)
                .isNew(this.isNew)
                .build();
    }
}
