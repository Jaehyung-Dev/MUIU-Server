package com.bit.muiu.service.impl;

import com.bit.muiu.entity.CustomUserDetails;
import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    // Spring Security 인증 과정에서 자동으로 호출되는 메소드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByUsername(username);

        // 리턴타입이 UserDetauls 형태이므로 구현해놓은 CustomUserDetails 형태로 구현
        return CustomUserDetails.builder()
                .member(member.orElseThrow(() -> new RuntimeException("member not exist"))).build();
    }
}
