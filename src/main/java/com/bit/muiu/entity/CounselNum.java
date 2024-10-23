package com.bit.muiu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "counsel_num")
@Getter
@Setter
@NoArgsConstructor
public class CounselNum {
    @Id
    @Column(name = "auth_num") // 데이터베이스의 컬럼명이 auth_num인 경우 명시적으로 매핑
    private String authNum;
}
