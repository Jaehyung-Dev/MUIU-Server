package com.bit.muiu.call;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerMessage {
    private String roomId;   // 방 ID
    private String sdp;      // SDP (세션 설명 프로토콜)
    private String type;     // 메시지 유형 ("answer")
}