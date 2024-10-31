package com.bit.muiu.entity;

import com.bit.muiu.config.ApplicationContextProvider;
import com.bit.muiu.dto.ChatRoomDto;
import com.bit.muiu.service.CounselService;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@SequenceGenerator(
        name = "chatRoomSeqGenerator",
        sequenceName = "ChatRoom_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chatRoomSeqGenerator"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "counselor", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Member counselor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client", referencedColumnName = "id", nullable = true)
    @JsonIgnore
    private Member client;

    private String status;

    public ChatRoomDto toDto() {
        return ChatRoomDto.builder()
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
                .counselId(this.id)
                .type("chat")
                .counselorId(this.counselor != null ? this.counselor.getId() : null)
                .clientId(this.client != null ? this.client.getId() : null)
                .build();
    }

//    @PostPersist
//    public void afterInsert() {
//        CounselService counselService = ApplicationContextProvider.getBean(CounselService.class);
//        counselService.saveCounsel(this.toCounsel());
//    }

}
