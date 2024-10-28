package com.bit.muiu.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatPartnerDto {
    private String name;
    private String role;

    public ChatPartnerDto(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // Getters and setters 생략
}

