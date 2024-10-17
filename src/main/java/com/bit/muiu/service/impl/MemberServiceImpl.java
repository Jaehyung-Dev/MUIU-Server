package com.bit.muiu.service.impl;

import com.bit.muiu.dto.MemberDto;
import com.bit.muiu.entity.Member;
import com.bit.muiu.jwt.JwtProvider;
import com.bit.muiu.repository.MemberRepository;
import com.bit.muiu.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Map<String, String> usernameCheck(String username) {
        Map<String, String> userCheckMsgMap = new HashMap<>();

        long usernameCheck = memberRepository.countByUsername(username);

        if(usernameCheck == 0)
            userCheckMsgMap.put("usernameCheckMsg", "available username");
        else
            userCheckMsgMap.put("usernameCheckMsg", "invalid username");

        return userCheckMsgMap;
    }

    @Override
    public MemberDto join(MemberDto memberDto) {
        memberDto.setRole("ROLE_USER");
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        MemberDto joinedMemberDto = memberRepository.save(memberDto.toEntity()).toDto();

        joinedMemberDto.setPassword("");

        return joinedMemberDto;
    }

    @Override
    public MemberDto login(MemberDto memberDto) {
        Member member = memberRepository.findByUsername(memberDto.getUsername()).orElseThrow(
                () -> new RuntimeException("username not exist")
        );

        if(!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new RuntimeException("wrong password");
        }

        MemberDto loginMemberDto = member.toDto();

        loginMemberDto.setPassword("");
        loginMemberDto.setToken(jwtProvider.createJwt(member));

        return loginMemberDto;
    }

    @Override
    public MemberDto getUserById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        return member.toDto();
    }

    @Override
    public MemberDto getUsernameAndRoleById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }

    @Override
    public void changePassword(Long id, String currentPassword, String newPassword) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new RuntimeException("기존 비밀번호와 일치하지 않습니다.");
        }

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }


}