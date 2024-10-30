package com.bit.muiu.service.impl;

import com.bit.muiu.entity.Member;
import com.bit.muiu.jwt.JwtProvider;
import com.bit.muiu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class NaverServiceImpl{
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.secretKey}")
    private String secretKey;

    public ResponseEntity<Object> processNaverLogin(String code, String state) {
        // WebClient 설정
        WebClient webClient = WebClient.builder()
                .baseUrl("https://nid.naver.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // Token API 호출
        Map<String, Object> tokenResponse = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2.0/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)  // Client ID
                        .queryParam("client_secret", secretKey)         // Client Secret
                        .queryParam("code", code)
                        .queryParam("state", state)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // Access Token 추출
        String accessToken = (String) tokenResponse.get("access_token");

        // AccessToken으로 사용자 정보 조회
        WebClient naverApiWebClient = WebClient.builder()
                .baseUrl("https://openapi.naver.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // 사용자 정보 요청
        Map<String, Object> infoResponse = naverApiWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/nid/me")
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/xml;charset=utf-8")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // 사용자 정보에서 필요한 항목 추출
        Map<String, String> infoResponseMap = (Map<String, String>) infoResponse.get("response");

        // 사용자 정보 파싱
        String email = infoResponseMap.get("email");
        String name = infoResponseMap.get("name");
        String mobile = infoResponseMap.get("mobile");
        String username = "naver_" + infoResponseMap.get("id");
        String gender = infoResponseMap.get("gender"); // 성별 정보 추가
        String birthDay = infoResponseMap.get("birthday"); // 생일 정보 추가 (예: MM-DD 형식)
        String birthYear = infoResponseMap.get("birthyear"); // 생년 정보 추가 (예: YYYY)

        if (mobile != null) {
            mobile = mobile.replace("-", "");
        }

        // 성별을 읽기 쉽게 변환 ("M" -> "남", "F" -> "여")
        if ("M".equals(gender)) {
            gender = "남";
        } else if ("F".equals(gender)) {
            gender = "여";
        }

        // 생년월일 조합 (YYYY-MM-DD 형식)
        Date birth = null;

        if (birthYear != null && birthDay != null) {
            String birthString = birthYear + "-" + birthDay; // "YYYY-MM-DD" 형식
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                birth = dateFormat.parse(birthString); // 문자열을 Date 객체로 변환
            } catch (Exception e) {
                log.error("Error parsing birth date: {}", e.getMessage());
            }
        }

        // 기존 사용자 조회
        Optional<Member> existingMember = memberRepository.findByUsername(username);
        Member member;
        boolean isNewMember = false;

        if (existingMember.isPresent()) {
            member = existingMember.get();
        } else {
            // 신규 사용자 처리
            isNewMember = true;
            member = Member.builder()
                    .username(username)
                    .password(passwordEncoder.encode(username)) // 비밀번호는 username을 사용해 인코딩
                    .email(email)
                    .name(name)
                    .tel(mobile)
                    .gender(gender)
                    .birth(birth)
                    .role("ROLE_USER")
                    .locationAgree(false)
                    .recordConsent(false)
                    .build();

            // 사용자 저장
            memberRepository.save(member);
        }

        // JWT 토큰 생성
        String token = jwtProvider.createJwt(member);

        // 사용자 정보를 Map에 저장하여 반환
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("isLogin", true); // 항상 로그인 성공으로 처리
        responseMap.put("id", member.getId()); // 사용자 ID
        responseMap.put("username", member.getUsername()); // 사용자 이름
        responseMap.put("role", member.getRole()); // 사용자 역할
        responseMap.put("token", token); // JWT 토큰
        responseMap.put("isNewMember", String.valueOf(isNewMember)); // 신규 사용자 여부

        // 결과 반환
        return ResponseEntity.ok(responseMap);
    }
}
