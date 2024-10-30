package com.bit.muiu.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ChatPartnerDto {
    private String name;
    private String role;
    private Long id;
}

