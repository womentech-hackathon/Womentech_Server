package com.womentech.server.configuration;

import com.womentech.server.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token.access-expire-ms}")
    private long accessExpireMs;

    @Value("${jwt.token.refresh-expire-ms}")
    private long refreshExpireMs;

    /**
     * Access 토큰 생성
     */
    public String createAccessJwt(Authentication authentication){
        String username = authentication.getName();

        return createJwt(username, secretKey, accessExpireMs);
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshJwt(Authentication authentication){
        String username = authentication.getName();

        String refreshToken = createJwt(username, secretKey, refreshExpireMs);

        // redis에 저장
        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                refreshExpireMs,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    /**
     * 토큰 생성
     */
    public String createJwt(String username, String secretKey, long expireMs) {
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
    public boolean isJwtExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}
