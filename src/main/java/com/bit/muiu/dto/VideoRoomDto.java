package com.bit.muiu.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoRoomDto {
    private Long id;
    private Long counselor;
    private Long client;
    private String status;

    private String counselorName;
    private String clientName;
}
