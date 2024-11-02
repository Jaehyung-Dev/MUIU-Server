package com.bit.muiu.entity;

import com.bit.muiu.config.ApplicationContextProvider;
import com.bit.muiu.dto.VideoRoomDto;
import com.bit.muiu.service.CounselService;
import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "videoRoomSeqGenerator",
        sequenceName = "VideoRoom_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoRoom {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "videoRoomSeqGenerator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor", referencedColumnName = "id", nullable = false)
    private Member counselor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client", referencedColumnName = "id", nullable = true)
    private Member client;

    private String status;

    public VideoRoomDto toDto() {
        return VideoRoomDto.builder()
                .id(this.id)
                .client(this.client != null ? this.client.getId() : 0L)
                .counselor(this.counselor.getId())
                .status(this.status)
                .counselorName(this.counselor.getName())
                .clientName(this.client != null ? this.client.getName() : "")
                .build();
    }

    public Counsel toCounsel() {
        return Counsel.builder()
                .counselId(this.id) // video_room의 ID
                .type("video")
                .counselorId(this.counselor != null ? this.counselor.getId() : null)
                .clientId(this.client != null ? this.client.getId() : null)
                .build();
    }

//    @PostPersist
//    public void afterInsert() {
//        CounselService counselService = ApplicationContextProvider.getBean(CounselService.class);
//        // counselService.saveCounsel(this.toCounsel());
//    }

}
