package com.bit.muiu.dto;

public class ChatMessageDto {
    private String channelId;
    private String content;
    private String sender;

    // 기본 생성자
    public ChatMessageDto() {}

    // 매개변수가 있는 생성자
    public ChatMessageDto(String channelId, String content, String sender) {
        this.channelId = channelId;
        this.content = content;
        this.sender = sender;
    }

    // Getter와 Setter
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
