package com.bit.muiu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportRequestDto {
    private String name;
    private String email;  // 사용자가 연락받을 이메일
    private String message;
}
