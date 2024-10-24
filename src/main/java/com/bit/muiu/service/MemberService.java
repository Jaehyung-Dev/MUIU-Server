package com.bit.muiu.service;

import com.bit.muiu.dto.MemberDto;
import com.bit.muiu.entity.Member;

import java.util.Map;

public interface MemberService {
    Map<String, String> usernameCheck(String username);

    MemberDto join(MemberDto memberDto);

    MemberDto login(MemberDto memberDto);

    MemberDto getUserById(Long id);

    MemberDto getUsernameAndRoleById(Long id);

    void changePassword(Long id, String currentPassword, String newPassword);

    void deleteUser(Long id);

    Member getMemberByToken(String token);

    boolean isEqual(String verifyNumber);

    MemberDto getNameById(Long id);
}
