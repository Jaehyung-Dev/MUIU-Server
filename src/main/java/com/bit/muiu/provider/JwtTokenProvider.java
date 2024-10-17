package com.bit.muiu.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenProvider {
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 자동으로 강력한 키 생성

    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1시간 유효

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
