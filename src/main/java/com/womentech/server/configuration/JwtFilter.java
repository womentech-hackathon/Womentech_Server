package com.womentech.server.configuration;

import com.womentech.server.service.UserDetailsServiceImpl;
import com.womentech.server.util.JwtUtil;
import com.womentech.server.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization : {}", authorization);

        // Token이 없을 경우 block
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("Authorization을 잘못 보냈습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // Token 꺼내기
        String token = authorization.split(" ")[1];

        // 로그아웃 되었는지 확인
        if (redisUtil.hasKeyBlackList(token)) {
            log.error("로그아웃 되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // Token Expired 되었는지 확인
        if (jwtUtil.isTokenExpired(token)) {
            log.error("Token이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // username Token에서 꺼내기
        String username = jwtUtil.getUsername(token);
        log.info("Username : {}", username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Detail 추가
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
