package com.bit.muiu.call;

public class CallRoomDto {

    private Long counselorId;
    private Long clientId;

    public CallRoomDto() {
    }

    public CallRoomDto(Long counselorId, Long clientId) {
        this.counselorId = counselorId;
        this.clientId = clientId;
    }

    public Long getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(Long counselorId) {
        this.counselorId = counselorId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
