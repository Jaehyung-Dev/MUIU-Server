package com.bit.muiu.config;

import com.bit.muiu.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration // 스프링에서 Configuration 클래스로 관리되기 위한 어노테이션
@EnableWebSecurity // Spring Security의 보안 설정을 커스터마이징할 수 있는 구성을 활성화
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(httpSecurityCorsConfigurer -> {}) // 커스터마이징 없이 기본 CORS 설정 사용
                .csrf(AbstractHttpConfigurer::disable) // JWT 토큰 인증 방식은 csrf 방지 설정이 필요없음
                .httpBasic(httpSecurityHttpBasicConfigurer -> {
                    httpSecurityHttpBasicConfigurer.disable();
                })
                .sessionManagement(httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers(
                            "/members/username-check",
                            "/members/join",
                            "/members/login",
                            "/api/disaster-messages/category",
                            "/members/{id}/**",
                            "/apis/profile/**",
                            "/sms/send/**",
                            "/ws/**", // WebSocket 경로 추가
                            "/topic/**", // STOMP 토픽 경로 추가
                            "/members/counselNum/**",
                            "/members/naver-callback",
                            "/chat/partner/**",
                            "/diaries/user/**",
                            "/app/chat/**",
                            "/chat/**",
                            "/api/call/**",
                            "/call/**",
                            "/my-websocket",
                            "/mind-column").permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })
                .addFilterAt(jwtAuthenticationFilter, CorsFilter.class)
                .build();
    }
}