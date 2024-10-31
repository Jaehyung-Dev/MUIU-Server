package com.bit.muiu.entity;

import com.bit.muiu.dto.MindColumnDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@SequenceGenerator(
        name = "mindColumnSeqGenerator",
        sequenceName = "MIND_COLUMN_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MindColumn {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "mindColumnSeqGenerator"
    )
    private Long mc_id;
    private String mc_title;
    private Long mc_img_num;
    private LocalDateTime regdate;
    @OneToMany(mappedBy = "mindColumn", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MindColumnFile> mcfList;

    public MindColumnDto toDto(){
        return MindColumnDto.builder()
                .mc_id(this.mc_id)
                .mc_title(this.mc_title)
                .mc_img_num(this.mc_img_num)
                .regdate(this.regdate)
                .mcfList(
                        mcfList != null && mcfList.size() > 0
                                ?     mcfList.stream().map(MindColumnFile::toDto).toList()
                                : new ArrayList<>()
                )
                .build();
    }

}
