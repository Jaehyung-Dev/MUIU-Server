package com.bit.muiu.service;

import com.bit.muiu.dto.MemberDto;

import java.util.Map;

public interface MemberService {
    Map<String, String> usernameCheck(String username);

    MemberDto join(MemberDto memberDto);

    MemberDto login(MemberDto memberDto);

    MemberDto getUserById(Long id);

    MemberDto getUsernameAndRoleById(Long id);
}
