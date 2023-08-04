package com.womentech.server.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    private final RedisUtil redisUtil;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.access-expire-ms}")
    private long accessExpireMs;

    @Value("${jwt.token.refresh-expire-ms}")
    private long refreshExpireMs;

    /**
     * Access 토큰 생성
     */
    public String createAccessToken(Authentication authentication){
        String username = authentication.getName();

        return createToken(username, secretKey, accessExpireMs);
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Authentication authentication){
        String username = authentication.getName();
        String refreshToken = createToken(username, secretKey, refreshExpireMs);

        redisUtil.set(authentication.getName(), refreshToken, refreshExpireMs);

        return refreshToken;
    }

    /**
     * 토큰 생성
     */
    public String createToken(String username, String secretKey, long expireMs) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 토큰으로부터 username을 꺼내어 반환
     */
    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    /**
     * 토큰이 만료되었는지 확인
     */
    public boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // 토큰의 만료 날짜 얻기
    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    // 토큰으로부터 클레임(claim) 얻기
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
