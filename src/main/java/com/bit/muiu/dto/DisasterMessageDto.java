package com.bit.muiu.dto;

import com.bit.muiu.entity.Diary;
import com.bit.muiu.entity.DisasterMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisasterMessageDto {
    private Long id;
    private String category;
    private String alertLevel;
    private String eventContent;
    private String occurrenceTime;
    private String readStatus;
    private String messageContent;

    public DisasterMessage toEntity() {
        return DisasterMessage.builder()
                .id(this.id)
                .category(this.category)
                .alertLevel(this.alertLevel)
                .alertLevel(this.alertLevel)
                .occurrenceTime(this.occurrenceTime)
                .readStatus(this.readStatus)
                .messageContent(this.messageContent)
                .build();
    }
}

