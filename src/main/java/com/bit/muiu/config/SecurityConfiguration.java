package com.bit.muiu.config;

import com.bit.muiu.entity.CustomUserDetails;
import com.bit.muiu.jwt.JwtAuthenticationFilter;
import com.bit.muiu.service.impl.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2UserServiceImpl oAuth2UserService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//                .cors(httpSecurityCorsConfigurer -> {})
                .csrf(AbstractHttpConfigurer::disable)
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
                            "/members/counselNum/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(oAuth2UserService)) // 사용자 정보 서비스 설정
                        .successHandler((request, response, authentication) -> {
                            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                            String token = userDetails.getToken();
                            boolean isNewMember = userDetails.isNewMember();

                            // 인증 성공 시 프론트엔드로 리다이렉트
                            if (isNewMember) {
                                // 신규 회원일 경우 join-success 페이지로 리다이렉트
                                response.sendRedirect("http://localhost:3000/join-success?token=" + token);
                            } else {
                                // 기존 회원일 경우 메인 페이지로 리다이렉트
                                response.sendRedirect("http://localhost:3000/main?token=" + token);
                            }
                        }))
                .addFilterAt(jwtAuthenticationFilter, CorsFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class)
                .build();
    }
}