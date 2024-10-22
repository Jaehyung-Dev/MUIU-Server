package com.bit.muiu.service.impl;

import com.bit.muiu.entity.CustomUserDetails;
import com.bit.muiu.entity.Member;
import com.bit.muiu.jwt.JwtProvider;
import com.bit.muiu.provider.NaverUserInfo;
import com.bit.muiu.provider.OAuth2UserInfo;
import com.bit.muiu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2UserServiceImpl.class);
//    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProvider jwtProvider;
    /*
     * 소셜 로그인 버튼 클릭 -> 인증서버로 요청 -> 인증서버에서 인증코드 발급 -> 발급받은 인증코드를 다시 한번 인증서버로 요청
     * -> 인증서버는 인증코드의 유효성 검사 후 토큰 발급 -> 발급받은 토큰으로 자원서버에 요청 -> 자원서버는 토큰의 유효성을 검사 후 사용자 정보 리턴
     * -> SecurityConfiguration에 설정된 userEndpoint에 지정된 Service 클래스의 loadUser 메소드가 자동 호출되면서 커스터마이징된 사용자 정보 리턴
     * */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request){
        OAuth2User oAuth2User = super.loadUser(request);
        String providerId = "";
        
        // 커스터마이징해서 사용자 정보를 받아줄 객체 생성
        OAuth2UserInfo oAuth2UserInfo = null;
        
        // 소셜 카테고리 검증
        if(request.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());

            providerId = oAuth2UserInfo.getProviderId();
        } else if (request.getClientRegistration().getRegistrationId().equals("kakao")) {

        } else if(request.getClientRegistration().getRegistrationId().equals("google")) {

        } else {
            return null;
        }

        String provider = oAuth2UserInfo.getProvider();
        String username = oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId();
        String password = passwordEncoder.encode(username);
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        Date birth = oAuth2UserInfo.getBirth();
        String gender = oAuth2UserInfo.getGender();
        String mobile = oAuth2UserInfo.getMobile();

        // 소셜 로그인을 했던 이력이 있는지 검사할 Member 엔티티
        // 소셜 로그인을 했던 이력이 있으면 그대로 소셜 로그인을 진행하고
        // 소셜 로그인을 했던 이력이 없으면 게시판 DB에 사용자 정보를 저장
        Member member;
        boolean isNewMember = false;

        // 한 번의 findByUsername 호출로 중복을 방지
        Optional<Member> existingMember = memberRepository.findByUsername(username);

        if (existingMember.isPresent()) {
            member = existingMember.get();
        } else {
            // 신규 회원 처리
            isNewMember = true;
            member = Member.builder()
                    .username(username)
                    .password(password) // passwordEncoder.encode(nickname)
                    .email(email) // email 값
                    .name(name) // name 값
                    .birth(birth)
                    .gender(gender)
                    .tel(mobile)
                    .role("ROLE_USER")
                    .locationAgree(false)
                    .recordConsent(false)
                    .build();

            memberRepository.save(member);
        }
        String token = jwtProvider.createJwt(member);

        return CustomUserDetails.builder()
                .member(member)
                .token(token)
                .attributes(oAuth2User.getAttributes())
                .isNewMember(isNewMember)
                .build();
    }
}
