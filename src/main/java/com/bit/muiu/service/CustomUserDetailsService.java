package com.bit.muiu.service;

import com.bit.muiu.entity.Member;
import com.bit.muiu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new CustomUserDetails(member);
    }

    // 추가적으로 id로 사용자 로드하는 메소드
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        log.info("사용자 ID로 사용자 로드: {}", id);  // 로그 추가

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다: id = {}", id);  // 오류 로그 추가
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: id = " + id);
                });

        return new CustomUserDetails(member);
    }
}
